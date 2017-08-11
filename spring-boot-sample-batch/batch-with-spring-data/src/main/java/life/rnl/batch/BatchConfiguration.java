package life.rnl.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import life.rnl.batch.excel.ExcelReader;
import life.rnl.batch.excel.mapping.ExcelMapping;
import life.rnl.batch.excel.mapping.builder.ExcelMappingBuilder;
import life.rnl.domain.Contact;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Bean
	public ExcelReader reader(ExcelMappingBuilder excelMappingBuilder, @Value("classpath:Contacts.xlsx") Resource resource) {
		ExcelReader excelReader = new ExcelReader();
		
		ExcelMapping excelMapping = excelMappingBuilder.build(Contact.class);
		excelReader.setExcelMapping(excelMapping);
		
		excelReader.setResource(resource);
		
		return excelReader;
	}
}
