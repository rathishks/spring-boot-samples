package life.rnl.batch.excel.mapping;

import java.util.ArrayList;
import java.util.List;

public class ExcelMappingImpl implements ExcelMapping {
	private String sheetName;
	private List<ExcelMappingColumn> columns = new ArrayList<>();

	@Override
	public List<ExcelMappingColumn> getColumns() {
		return columns;
	}

	@Override
	public String getSheetName() {
		return sheetName;
	}

	@Override
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}