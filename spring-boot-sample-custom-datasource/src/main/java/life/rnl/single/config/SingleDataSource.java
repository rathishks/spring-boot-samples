package life.rnl.single.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SingleDataSource {

	/**
	 * This will cause
	 * {@link org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration}
	 * to back off on {@link DataSource} instantiation.
	 */
	@Bean
	public DataSource myDataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}
}
