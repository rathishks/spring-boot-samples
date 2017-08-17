package life.rnl.migration.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
import life.rnl.migration.source.domain.Asset;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Bean
	public ThreadPoolTaskExecutor multiThreadedTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

		taskExecutor.setCorePoolSize(5);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

		return taskExecutor;
	}

	@Bean
	public Job itemMigrationJob(JobBuilderFactory jobBuilderFactory, Step itemImportStep) {
		return jobBuilderFactory.get("itemMigration").start(itemImportStep).build();
	}

	@Bean
	public Step itemImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> itemReader,
			ItemProcessor<Asset, Item> itemProcessor, ItemWriter<Item> itemWriter,
			ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		return stepBuilderFactory.get("itemMigrationStep").<Asset, Item>chunk(1000).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).taskExecutor(multiThreadedTaskExecutor).build();
	}

	@Bean
	public ItemReader<Asset> itemReader(EntityManagerFactory sourceEntityManagerFactory,
			@Value("${itemImport.reader.query}") String readerQuery) {
		JpaPagingItemReader<Asset> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(100);

		return reader;
	}

	@Bean
	public ItemProcessor<Asset, Item> itemProcessor() {
		return (asset) -> {
			Item item = new Item();

			item.setDateCreated(asset.getDateCreated());
			item.setSerialNumber(asset.getSerialNumber());
			item.setAssetId(asset.getId());

			return item;
		};
	}

	@Bean
	public ItemWriter<Item> itemWriter(
			@Qualifier("destinationEntityManagerFactory") EntityManagerFactory destinationEntityManagerFactory) {
		JpaItemWriter<Item> itemWriter = new JpaItemWriter<>();

		itemWriter.setEntityManagerFactory(destinationEntityManagerFactory);

		return itemWriter;
	}
}