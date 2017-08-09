package life.rnl.batch.excel.mapping;

public class ExcelMappingColumn {
	private String columnName;
	private String fieldName;
	
	public ExcelMappingColumn(String fieldName, String columnName) {
		this.fieldName = fieldName;
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}