package life.rnl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.test.context.junit4.SpringRunner;

import life.rnl.multiple.firstdatasource.FirstEntity;
import life.rnl.multiple.seconddatasource.SecondEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultipleApplication.class)
public class MultipleDataSourceTests {

	@Autowired
	ApplicationContext appContext;

	@Autowired
	JpaContext jpaContext;

	@Test
	public void testDataSources() {
		Map<String, DataSource> beans = appContext.getBeansOfType(DataSource.class);
		assertThat(beans.size()).isEqualTo(2);

		assertThat(beans.get("myDataSource")).isNotNull();
		assertThat(beans.get("myDataSource2")).isNotNull();

		assertThat(beans.get("myDataSource")).isNotEqualTo(beans.get("myDataSource2"));
	}

	@Test
	public void testEntityManagers() {
		Map<String, EntityManagerFactory> beans = appContext.getBeansOfType(EntityManagerFactory.class);
		assertThat(beans.size()).isEqualTo(2);

		EntityManagerFactory entityManagerFactory = beans.get("entityManagerFactory");
		assertThat(entityManagerFactory).isNotNull();

		EntityManagerFactory entityManagerFactory2 = beans.get("entityManagerFactory2");
		assertThat(entityManagerFactory2).isNotNull();

		assertThat(entityManagerFactory).isNotEqualTo(entityManagerFactory2);

		assertThat(jpaContext.getEntityManagerByManagedType(FirstEntity.class).getEntityManagerFactory())
				.isEqualTo(entityManagerFactory);

		assertThat(jpaContext.getEntityManagerByManagedType(SecondEntity.class).getEntityManagerFactory())
				.isEqualTo(entityManagerFactory2);
	}
}