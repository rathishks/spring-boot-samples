package life.rnl.migration.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.SimplePartitioner;
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
	public Step itemPartitionedImportStep(StepBuilderFactory stepBuilderFactory, ItemReader<Asset> itemReader,
			@Qualifier("asyncItemProcessor") AsyncItemProcessor itemProcessor,
			@Qualifier("asyncItemWriter") AsyncItemWriter itemWriter, ProcessedIndicatorStepListener listener) {
		Step step = stepBuilderFactory.get("itemPartitionMigrationStep").<Asset, Item>chunk(100).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).listener(listener).build();

		return stepBuilderFactory.get("itemPartitionStep").partitioner("slaveStep", new SimplePartitioner()).step(step)
				.build();
	}

	@Bean(name = "asyncItemProcessor")
	public AsyncItemProcessor<Asset, Item> asyncItemProcessor(ItemProcessor<Asset, Item> itemProcessor,
			ThreadPoolTaskExecutor multiThreadedTaskExecutor) {
		AsyncItemProcessor<Asset, Item> asyncItemProcessor = new AsyncItemProcessor<>();

		asyncItemProcessor.setDelegate(itemProcessor);
		asyncItemProcessor.setTaskExecutor(multiThreadedTaskExecutor);

		return asyncItemProcessor;
	}

	@Bean
	public AsyncItemWriter<Item> asyncItemWriter(ItemWriter<Item> itemWriter) {
		AsyncItemWriter<Item> asyncItemWriter = new AsyncItemWriter<>();

		asyncItemWriter.setDelegate(itemWriter);

		return asyncItemWriter;
	}
}