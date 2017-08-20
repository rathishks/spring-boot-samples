package life.rnl.migration.source;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(basePackages = "life.rnl.migration.source", entityManagerFactoryRef = "sourceEntityManagerFactory", transactionManagerRef = "sourceTransactionManager")
public class SourceConfiguration {
	@Bean
	@ConfigurationProperties("spring.datasource")
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
	public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
			EntityManagerFactoryBuilder entityManagerFactoryBuilder, DataSource myDataSource,
			JpaProperties jpaProperties) {
		return entityManagerFactoryBuilder.dataSource(myDataSource).packages("life.rnl.migration.source")
				.properties(jpaProperties.getHibernateProperties(myDataSource)).persistenceUnit("source").build();
	}

	@Bean
	public JpaTransactionManager sourceTransactionManager(EntityManagerFactory sourceEntityManagerFactory) {
		return new JpaTransactionManager(sourceEntityManagerFactory);
	}
}