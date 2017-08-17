package life.rnl.migration.destination;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(basePackages = "life.rnl.migration.destination")
public class DestinationConfiguration {
	@Bean
	@ConfigurationProperties("spring.datasource.destination")
	public DataSource destinationDataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	public JpaVendorAdapter destinationJpaVendorAdapter(JpaProperties jpaProperties, DataSource destinationDataSource) {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(jpaProperties.isShowSql());
		adapter.setDatabase(jpaProperties.determineDatabase(destinationDataSource));
		adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		return adapter;
	}

	@Bean
	public EntityManagerFactoryBuilder destinationEntityManagerFactoryBuilder(
			JpaVendorAdapter destinationJpaVendorAdapter, ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
			JpaProperties jpaProperties) {
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(destinationJpaVendorAdapter,
				jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
		return builder;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory2(
			EntityManagerFactoryBuilder destinationEntityManagerFactoryBuilder, DataSource destinationDataSource,
			JpaProperties jpaProperties) {
		return destinationEntityManagerFactoryBuilder.dataSource(destinationDataSource)
				.packages("life.rnl.migration.destination")
				.properties(jpaProperties.getHibernateProperties(destinationDataSource)).build();
	}
}