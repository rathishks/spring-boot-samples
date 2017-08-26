package life.rnl.migration.batch.async;

import java.util.concurrent.Future;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.destination.domain.Part;
import life.rnl.migration.destination.repository.PartRepository;
import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.domain.PartType;

@Configuration
public class AsyncItemConfiguration {

	@Bean
	public Job asyncItemJob(JobBuilderFactory jobBuilderFactory, Step asyncItemImportStep, Step asyncPartImportStep) {
		return jobBuilderFactory.get("itemMigrationJob").flow(asyncItemImportStep).next(asyncPartImportStep).end()
				.build();
	}

	@Bean
	public Step asyncItemImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> itemReader,
			ItemProcessor<Asset, Future<Item>> asyncItemProcessor, ItemWriter<Future<Item>> asyncItemWriter,
			AsyncItemListener listener) {
		Step step = stepBuilderFactory.get("itemMigrationStep").<Asset, Future<Item>>chunk(5000).reader(itemReader)
				.processor(asyncItemProcessor).writer(asyncItemWriter).listener(listener).build();

		return step;
	}

	@Bean
	public ItemReader<Asset> itemReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${itemImport.reader.query}") String readerQuery) {
		JpaPagingItemReader<Asset> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(5000);

		return reader;
	}

	@Bean
	public ItemProcessor<Asset, Future<Item>> asyncItemProcessor(PartRepository partRepository,
			ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		AsyncItemProcessor<Asset, Item> asyncItemProcessor = new AsyncItemProcessor<>();

		AssetItemProcessor itemProcessor = new AssetItemProcessor();
		itemProcessor.setPartRepository(partRepository);

		asyncItemProcessor.setDelegate(itemProcessor);
		asyncItemProcessor.setTaskExecutor(multiThreadedTaskExecutor);

		return asyncItemProcessor;
	}

	@Bean
	public ItemWriter<Future<Item>> asyncItemWriter(ItemWriter<Item> itemWriter) {
		AsyncItemWriter<Item> asyncItemWriter = new AsyncItemWriter<>();

		asyncItemWriter.setDelegate(itemWriter);

		return asyncItemWriter;
	}

	@Bean
	public Step asyncPartImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<PartType> partTypeReader,
			ItemProcessor<PartType, Future<Part>> asyncPartTypeProcessor,
			ItemWriter<Future<Part>> asyncPartTypeWriter) {
		Step step = stepBuilderFactory.get("partMigrationStep").<PartType, Future<Part>>chunk(5000)
				.reader(partTypeReader).processor(asyncPartTypeProcessor).writer(asyncPartTypeWriter).build();

		return step;
	}

	@Bean
	public ItemReader<PartType> partTypeReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${partImport.reader.query}") String readerQuery) {
		JpaPagingItemReader<PartType> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(5000);

		return reader;
	}

	@Bean
	public ItemProcessor<PartType, Future<Part>> asyncPartTypeProcessor(
			ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		AsyncItemProcessor<PartType, Part> asyncPartProcessor = new AsyncItemProcessor<>();

		asyncPartProcessor.setDelegate((partType) -> {
			Part part = new Part();
			part.setPartNumber(partType.getPartNumber());
			return part;
		});
		asyncPartProcessor.setTaskExecutor(multiThreadedTaskExecutor);

		return asyncPartProcessor;
	}

	@Bean
	public ItemWriter<Future<Part>> asyncPartTypeWriter(EntityManagerFactory destinationEntityManagerFactory) {
		AsyncItemWriter<Part> asyncPartWriter = new AsyncItemWriter<>();

		JpaItemWriter<Part> partWriter = new JpaItemWriter<>();
		partWriter.setEntityManagerFactory(destinationEntityManagerFactory);

		asyncPartWriter.setDelegate(partWriter);

		return asyncPartWriter;
	}
}