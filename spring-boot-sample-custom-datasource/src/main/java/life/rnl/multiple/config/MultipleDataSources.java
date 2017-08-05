package life.rnl.multiple.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class MultipleDataSources {

	@Bean
	@ConfigurationProperties("spring.datasource")
	@Primary
	public DataSource myDataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties, DataSource myDataSource) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.determineDatabase(myDataSource));
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		return adapter;
	}

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager, JpaProperties jpaProperties) {
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter,
				jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
		return builder;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			EntityManagerFactoryBuilder entityManagerFactoryBuilder, DataSource myDataSource,
			JpaProperties jpaProperties) {
		return entityManagerFactoryBuilder.dataSource(myDataSource).packages("life.rnl.multiple.firstdatasource")
				.properties(jpaProperties.getHibernateProperties(myDataSource)).build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.second")
	public DataSource myDataSource2(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter2(JpaProperties jpaProperties, DataSource myDataSource2) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.determineDatabase(myDataSource2));
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		return adapter;
	}

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder2(JpaVendorAdapter jpaVendorAdapter2,
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager, JpaProperties jpaProperties) {
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter2,
				jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
		return builder;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory2(
			EntityManagerFactoryBuilder entityManagerFactoryBuilder2, DataSource myDataSource2,
			JpaProperties jpaProperties) {
		return entityManagerFactoryBuilder2.dataSource(myDataSource2).packages("life.rnl.multiple.seconddatasource")
				.properties(jpaProperties.getHibernateProperties(myDataSource2)).build();
	}
}
