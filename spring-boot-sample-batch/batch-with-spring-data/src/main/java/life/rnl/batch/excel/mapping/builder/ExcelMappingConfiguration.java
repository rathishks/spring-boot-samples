package life.rnl.batch.excel.mapping.builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelMappingConfiguration {
	@Bean
	public ExcelMappingBuilder excelMappingBuilder() {
		return () -> {
		};
	}
}