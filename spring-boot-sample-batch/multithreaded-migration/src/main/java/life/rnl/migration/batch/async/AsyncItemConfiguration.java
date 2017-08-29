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
		return jobBuilderFactory.get("itemMigrationJob").flow(asyncPartImportStep).next(asyncItemImportStep).end()
				.build();
	}

	@Bean
	public Step asyncItemImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> itemReader,
			ItemProcessor<Asset, Future<Item>> asyncItemProcessor, ItemWriter<Future<Item>> asyncItemWriter,
			AsyncItemListener listener, @Value("${import.chunk.size}") Integer chunkSize) {
		Step step = stepBuilderFactory.get("itemMigrationStep").<Asset, Future<Item>>chunk(chunkSize).reader(itemReader)
				.processor(asyncItemProcessor).writer(asyncItemWriter).listener(listener).build();

		return step;
	}

	@Bean
	public ItemReader<Asset> itemReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${itemImport.asyncreader.query}") String readerQuery,
			@Value("${import.chunk.size}") Integer chunkSize) {
		JpaPagingItemReader<Asset> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(chunkSize);

		return reader;
	}

	@Bean
	public ItemProcessor<Asset, Future<Item>> asyncItemProcessor(PartRepository partRepository,
			ThreadPoolTaskExecutor multiThreadedTaskExecutor, AssetItemProcessor processor) {
		AsyncItemProcessor<Asset, Item> asyncItemProcessor = new AsyncItemProcessor<>();

		asyncItemProcessor.setDelegate(processor);
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
			ItemProcessor<PartType, Future<Part>> asyncPartTypeProcessor, ItemWriter<Future<Part>> asyncPartTypeWriter,
			@Value("${import.chunk.size}") Integer chunkSize) {
		Step step = stepBuilderFactory.get("partMigrationStep").<PartType, Future<Part>>chunk(chunkSize)
				.reader(partTypeReader).processor(asyncPartTypeProcessor).writer(asyncPartTypeWriter).build();

		return step;
	}

	@Bean
	public ItemReader<PartType> partTypeReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${partImport.reader.query}") String readerQuery, @Value("${import.chunk.size}") Integer chunkSize) {
		JpaPagingItemReader<PartType> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(chunkSize);

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