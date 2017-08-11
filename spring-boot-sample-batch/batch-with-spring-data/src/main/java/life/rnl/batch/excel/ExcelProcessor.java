package life.rnl.batch.excel;

import javax.annotation.PostConstruct;

import org.springframework.batch.item.ItemProcessor;

import life.rnl.batch.excel.mapping.ExcelMapping;

public class ExcelProcessor<O> implements ItemProcessor<ExcelRow, O> {

	private ExcelMapping excelMapping;

	public void setExcelMapping(ExcelMapping excelMapping) {
		this.excelMapping = excelMapping;
	}

	@Override
	public O process(ExcelRow item) throws Exception {
		return null;
	}

	@PostConstruct
	public void postConstruct() throws Exception {
		if (this.excelMapping == null) {
			throw new Exception("An Excel Mapping must be specified for Excel Processor.");
		}
	}
}