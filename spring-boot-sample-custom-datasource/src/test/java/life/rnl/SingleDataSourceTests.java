package life.rnl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class SingleDataSourceTests {

	@Autowired
	ApplicationContext appContext;

	@Test
	public void testDataSources() {
		Map<String, DataSource> beans = appContext.getBeansOfType(DataSource.class);
		assertThat(beans.size()).isEqualTo(1);
		
		assertThat(beans.get("myDataSource")).isNotNull();
	}
}