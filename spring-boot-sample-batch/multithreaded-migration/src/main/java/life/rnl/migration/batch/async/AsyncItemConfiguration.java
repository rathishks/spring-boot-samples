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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;

@Configuration
public class AsyncItemConfiguration {

	@Bean
	public Job asyncItemJob(JobBuilderFactory jobBuilderFactory, Step asyncItemImportStep) {
		return jobBuilderFactory.get("itemMigrationPartitionedJob").start(asyncItemImportStep).build();
	}

	@Bean
	public Step asyncItemImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> asyncItemReader,
			ItemProcessor<Asset, Future<Item>> asyncItemProcessor, ItemWriter<Future<Item>> asyncItemWriter,
			AsyncItemListener listener, ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		Step step = stepBuilderFactory.get("itemPartitionMigrationStep").<Asset, Future<Item>>chunk(5000)
				.reader(asyncItemReader).processor(asyncItemProcessor).writer(asyncItemWriter).listener(listener)
				.build();

		return step;
	}

	@Bean
	public ItemReader<Asset> asyncItemReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${itemImport.reader.query}") String readerQuery) {
		JpaPagingItemReader<Asset> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(5000);

		return reader;
	}

	@Bean
	public ItemProcessor<Asset, Future<Item>> asyncItemProcessor(ItemProcessor<Asset, Item> itemProcessor,
			ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		AsyncItemProcessor<Asset, Item> asyncItemProcessor = new AsyncItemProcessor<>();

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
}