package life.rnl.migration.batch;

import java.util.concurrent.Future;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;

@Configuration
public class PartitionedBatchConfiguration {

	@Bean
	public Job itemMigrationPartitionedJob(JobBuilderFactory jobBuilderFactory, Step itemPartitionedImportStep) {
		return jobBuilderFactory.get("itemMigrationPartitionedJob").start(itemPartitionedImportStep).build();
	}

	@Bean
	public Step itemPartitionedImportStep(StepBuilderFactory stepBuilderFactory,
			ItemReader<Asset> synchronizedItemReader,
			@Qualifier("asyncItemProcessor") ItemProcessor<Asset, Future<Item>> asyncItemProcessor,
			@Qualifier("asyncItemWriter") ItemWriter<Future<Item>> asyncItemWriter,
			ProcessedIndicatorStepListener listener, ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		Step step = stepBuilderFactory.get("itemPartitionMigrationStep").<Asset, Future<Item>>chunk(100)
				.reader(synchronizedItemReader).processor(asyncItemProcessor).writer(asyncItemWriter).listener(listener).build();

		return step;
	}

	@Bean(name = "asyncItemProcessor")
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