package life.rnl.batch.excel.mapping.builder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelMappingConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public ExcelMappingBuilder excelMappingBuilder() {
		return () -> {
		};
	}
}