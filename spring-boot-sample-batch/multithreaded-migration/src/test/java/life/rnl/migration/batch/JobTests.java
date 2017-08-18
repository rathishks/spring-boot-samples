package life.rnl.migration.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JobTests {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job itemMigrationJob;

	@Autowired
	private Job synchronizedItemMigrationJob;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void testBeanCounts() {
		assertThat(applicationContext.getBeansOfType(EntityManagerFactoryBuilder.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(EntityManagerFactory.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(JpaVendorAdapter.class).size()).isEqualTo(2);
		assertThat(applicationContext.getBeansOfType(DataSource.class).size()).isEqualTo(2);
		// extra PlatformTransactionManager comes from SimpleBatchConfiguration
		assertThat(applicationContext.getBeansOfType(PlatformTransactionManager.class).size()).isEqualTo(3);

	}

	// not always reproducible
	@Test(expected = PersistenceException.class)
	public void testMigration() throws Throwable {
		JobExecution jobExecution = this.jobLauncher.run(this.itemMigrationJob, new JobParameters());
//		while (jobExecution.getAllFailureExceptions().isEmpty()) {
//			itemRepository.deleteAll();
//			jobExecution = this.jobLauncher.run(this.itemMigrationJob, new JobParameters());
//		}

		if (!jobExecution.getAllFailureExceptions().isEmpty()) {
			throw jobExecution.getAllFailureExceptions().iterator().next();
		}
	}

	@Test
	public void testSynchronizedMigration() throws Exception {
		this.jobLauncher.run(this.synchronizedItemMigrationJob, new JobParameters());

		List<Item> items = itemRepository.findAll();
		assertThat(items.size()).isEqualTo(25000);
	}
}