package life.rnl.migration.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;

@Configuration
public class MultiThreadedStepConfiguration {

	@Bean
	public Job multithreadedJob(JobBuilderFactory jobBuilderFactory, Step synchronizedItemImportStep) {
		return jobBuilderFactory.get("synchronizedItemMigrationJob").start(synchronizedItemImportStep).build();
	}

	@Bean
	public Step synchronizedItemImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> itemReader,
			ItemProcessor<Asset, Item> itemProcessor, ItemWriter<Item> itemWriter,
			ProcessedIndicatorStepListener listener, ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		return stepBuilderFactory.get("synchronizedItemMigrationStep").<Asset, Item>chunk(12000).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).listener(listener).taskExecutor(multiThreadedTaskExecutor)
				.build();
	}

	@Bean
	public ItemReader<Asset> itemReader(
			@Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
			@Value("${itemImport.reader.query}") String readerQuery) {
		SynchronizedItemStreamReader<Asset> syncReader = new SynchronizedItemStreamReader<>();

		JpaPagingItemReader<Asset> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(sourceEntityManagerFactory);
		reader.setQueryString(readerQuery);
		reader.setPageSize(10000);
		reader.setSaveState(false);

		syncReader.setDelegate(reader);

		return syncReader;
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
	public ItemWriter<Item> itemWriter(EntityManagerFactory destinationEntityManagerFactory) {
		JpaItemWriter<Item> itemWriter = new JpaItemWriter<>();

		itemWriter.setEntityManagerFactory(destinationEntityManagerFactory);

		return itemWriter;
	}
}