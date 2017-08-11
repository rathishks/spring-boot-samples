package life.rnl.batch.excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import life.rnl.batch.excel.mapping.ExcelMapping;
import life.rnl.batch.excel.validator.RowValidator;

public class ExcelReader extends AbstractItemCountingItemStreamItemReader<ExcelRow> {
	private Resource resource;
	private ExcelMapping excelMapping;
	private RowValidator rowValidator;

	private XSSFWorkbook workbook;
	private Iterator<Row> rowIterator;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setExcelMapping(ExcelMapping excelMapping) {
		this.excelMapping = excelMapping;
	}

	public void setRowValidator(RowValidator rowValidator) {
		this.rowValidator = rowValidator;
	}

	@Override
	protected ExcelRow doRead() throws Exception {
		if (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			boolean valid = rowValidator.validate(row);
			if (!valid) {
				while (rowIterator.hasNext()) {
					row = rowIterator.next();

					if (rowValidator.validate(row)) {
						return new ExcelRow(rowIterator.next());
					}
				}
			}
		}

		return null;
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(resource, "A Resource must be specified for ExcelReader.");

		workbook = new XSSFWorkbook(resource.getInputStream());
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);

		workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

		Sheet sheet = workbook.getSheet(excelMapping.getSheetName());
		Assert.notNull(sheet, String.format("The workbook (%s) does not contain the specified sheet %s",
				resource.getFilename(), excelMapping.getSheetName()));

		rowIterator = sheet.rowIterator();
		if (rowIterator.hasNext()) {
			rowIterator.next();

			int currentItemCount = getCurrentItemCount();
			for (int i = 0; i < currentItemCount; i++) {
				if (rowIterator.hasNext()) {
					rowIterator.next();
				} else {
					break;
				}
			}
		}

		if (rowValidator == null) {
			rowValidator = (row) -> {
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					Cell cell = cells.next();
					if (cell != null) {
						return true;
					}
				}

				return false;
			};
		}
	}

	@Override
	protected void doClose() throws Exception {
		if (resource != null) {
			resource.getInputStream().close();
		}
	}
}