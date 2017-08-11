package life.rnl.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import life.rnl.batch.excel.ExcelProcessor;
import life.rnl.batch.excel.ExcelReader;
import life.rnl.batch.excel.mapping.ExcelMapping;
import life.rnl.batch.excel.mapping.builder.ExcelMappingBuilder;
import life.rnl.domain.Contact;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Bean
	public ExcelMapping contactMapping(ExcelMappingBuilder excelMappingBuilder) {
		ExcelMapping excelMapping = excelMappingBuilder.build(Contact.class);
		return excelMapping;
	}

	@Bean
	public ExcelReader reader(ExcelMapping contactMapping, @Value("classpath:Contacts.xlsx") Resource resource) {
		ExcelReader excelReader = new ExcelReader();

		excelReader.setExcelMapping(contactMapping);
		excelReader.setResource(resource);

		return excelReader;
	}

	@Bean
	public ExcelProcessor<Contact> processor(ExcelMapping contactMapping) {
		ExcelProcessor<Contact> excelProcessor = new ExcelProcessor<>();

		excelProcessor.setExcelMapping(contactMapping);

		return excelProcessor;
	}
}
