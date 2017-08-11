package life.rnl.batch.excel;

import org.apache.poi.ss.usermodel.Row;

public class ExcelRow {
	private Row row;
	
	public ExcelRow(Row row) {
		this.row = row;
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}
}
