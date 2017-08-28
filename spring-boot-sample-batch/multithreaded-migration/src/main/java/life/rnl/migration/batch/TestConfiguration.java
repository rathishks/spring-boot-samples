package life.rnl.migration.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

	@Bean
	public TestComponent testComponent1() {
		return new TestComponent();
	}
	
	@Bean
	public TestComponent testComponent2(TestComponent testComponent) {
		return testComponent;
	}
}
