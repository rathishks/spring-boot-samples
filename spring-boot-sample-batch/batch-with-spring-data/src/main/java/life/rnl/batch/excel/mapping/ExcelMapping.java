package life.rnl.batch.excel.mapping;

import java.util.List;

public interface ExcelMapping {
	List<ExcelMappingColumn> getColumns();

	String getSheetName();

	void setSheetName(String sheetName);
	
	void setReturnClass(Class<?> clazz);
	
	Class<?> getReturnClass();
}