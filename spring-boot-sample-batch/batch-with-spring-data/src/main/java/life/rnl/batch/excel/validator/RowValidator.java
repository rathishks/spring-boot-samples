package life.rnl.batch.excel.validator;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface RowValidator {
	boolean validate(Row row);
}