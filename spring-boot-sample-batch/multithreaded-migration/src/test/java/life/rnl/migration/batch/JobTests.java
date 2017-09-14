package life.rnl.migration.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import life.rnl.migration.Application;
import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.destination.repository.ItemRepository;
import life.rnl.migration.source.domain.ProcessedStatus;
import life.rnl.migration.source.repository.AssetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JobTests {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job multithreadedJob;

	@Autowired
	private Job asyncItemJob;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private AssetRepository assetRepository;

	@Test
	public void testBeanCounts() {
		assertThat(applicationContext.getBeansOfType(EntityManagerFactoryBuilder.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(EntityManagerFactory.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(JpaVendorAdapter.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(DataSource.class).size()).isEqualTo(2);
		// extra PlatformTransactionManager comes from SimpleBatchConfiguration
		assertThat(applicationContext.getBeansOfType(PlatformTransactionManager.class).size()).isEqualTo(3);

	}

	@Test
	public void testMultiThreadedMigration() throws Exception {
		this.jobLauncher.run(this.multithreadedJob, new JobParameters());

		List<Item> items = itemRepository.findAll();
		assertThat(items.size()).isEqualTo(50000);

		assertThat(assetRepository.countByProcessed(ProcessedStatus.UNREAD)).isEqualTo(0);
		assertThat(assetRepository.countByProcessed(ProcessedStatus.WRITTEN)).isEqualTo(50000);
	}

	@Test
	public void testAsyncMigration() throws Exception {
		this.jobLauncher.run(this.asyncItemJob, new JobParameters());
		List<Item> items = itemRepository.findAll();
		assertThat(items.size()).isEqualTo(50000);
		items.parallelStream().forEach(item -> {
			assertThat(item.getPart()).isNotNull();
		});
	}

	@Test
	public void testComponent() {
		TestComponent testComponent1 = (TestComponent) applicationContext.getBean("testComponent1");
		assertThat(testComponent1).isNotNull();
		assertThat(testComponent1.getPartRepository()).isNotNull();

		TestComponent testComponent2 = (TestComponent) applicationContext.getBean("testComponent2");
		assertThat(testComponent2).isNotNull();
		assertThat(testComponent2.getPartRepository()).isNotNull();
		
		TestComponent testComponent = (TestComponent) applicationContext.getBean("testComponent");
		assertThat(testComponent).isNotNull();
		assertThat(testComponent.getPartRepository()).isNotNull();
		
		assertThat(applicationContext.getBeansOfType(TestComponent.class).size()).isEqualTo(3);
	}
}