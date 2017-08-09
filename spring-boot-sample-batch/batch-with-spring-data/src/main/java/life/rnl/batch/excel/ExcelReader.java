package life.rnl.batch.excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import life.rnl.batch.excel.mapping.ExcelMapping;

public class ExcelReader extends AbstractItemCountingItemStreamItemReader<ExcelRow> {
	private Resource resource;
	private ExcelMapping excelMapping;

	private XSSFWorkbook workbook;
	private Iterator<Row> rowIterator;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setExcelMapping(ExcelMapping excelMapping) {
		this.excelMapping = excelMapping;
	}

	@Override
	protected ExcelRow doRead() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(this.resource, "A Resource must be specified for ExcelReader.");

		this.workbook = new XSSFWorkbook(this.resource.getInputStream());

		Sheet sheet = this.workbook.getSheet(excelMapping.getSheetName());
		Assert.notNull(sheet, String.format("The workbook (%s) does not contain the specified sheet %s",
				this.resource.getFilename(), excelMapping.getSheetName()));

		this.rowIterator = sheet.rowIterator();
		if (this.rowIterator.hasNext()) {
			this.rowIterator.next();

			int currentItemCount = getCurrentItemCount();
			for (int i = 0; i < currentItemCount; i++) {
				if (this.rowIterator.hasNext()) {
					this.rowIterator.next();
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);

	}

	@Override
	protected void doClose() throws Exception {
		if (this.resource != null) {
			this.resource.getInputStream().close();
		}
	}
}