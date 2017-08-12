package life.rnl.batch.excel.mapping;

import java.util.ArrayList;
import java.util.List;

public class ExcelMappingImpl implements ExcelMapping {
	private String sheetName;
	private List<ExcelMappingColumn> columns = new ArrayList<>();
	private Class<?> returnClass;

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

	@Override
	public void setReturnClass(Class<?> clazz) {
		this.returnClass = clazz;
	}

	@Override
	public Class<?> getReturnClass() {
		return this.returnClass;
	}
}