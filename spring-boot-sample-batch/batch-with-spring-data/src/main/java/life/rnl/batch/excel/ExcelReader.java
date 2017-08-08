package life.rnl.batch.excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ExcelReader extends AbstractItemCountingItemStreamItemReader<ExcelRow> {
	private Resource resource;
	private XSSFWorkbook workbook;
	
	private Iterator<Row> rowIterator;

	public void setResource(Resource resource) {
		this.resource = resource;
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
	}

	@Override
	protected void doClose() throws Exception {
		if (this.resource != null) {
			this.resource.getInputStream().close();
		}
	}
}