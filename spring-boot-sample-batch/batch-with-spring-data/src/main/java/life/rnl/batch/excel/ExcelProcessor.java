package life.rnl.batch.excel;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.item.ItemProcessor;

import life.rnl.batch.excel.mapping.ExcelMapping;

public class ExcelProcessor<O> implements ItemProcessor<ExcelRow, O> {

	private ExcelMapping excelMapping;

	public void setExcelMapping(ExcelMapping excelMapping) {
		this.excelMapping = excelMapping;
	}

	@SuppressWarnings("unchecked")
	@Override
	public O process(ExcelRow item) throws Exception {
		Class<?> returnClass = excelMapping.getReturnClass();
		O processedItem = (O) returnClass.newInstance();
		
		Row row = item.getRow();
		excelMapping.getColumns().forEach((column) -> {
			
		});

		return processedItem;
	}

	@PostConstruct
	public void postConstruct() throws Exception {
		if (this.excelMapping == null) {
			throw new Exception("An Excel Mapping must be specified for Excel Processor.");
		}
	}
}