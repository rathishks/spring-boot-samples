package life.rnl.autowiring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import life.rnl.autowiring.AutowiringScenarios.TestA;
import life.rnl.autowiring.AutowiringScenarios.TestB;
import life.rnl.autowiring.AutowiringScenarios.TestBean;
import life.rnl.autowiring.AutowiringScenarios.TestD.TestInjectionDBean;
import life.rnl.autowiring.AutowiringScenarios.TestInjectionBean;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutowiringScenariosTests {

	@Autowired
	ApplicationContext appContext;

	@Test
	public void testA() {
		TestA testA = appContext.getBean(TestA.class);
		assertThat(testA).isNotNull();
		assertThat(testA.getApplicationContext()).isNotNull();
	}

	@Test
	public void testB() {
		TestB testB = appContext.getBean(TestB.class);
		assertThat(testB).isNotNull();
		assertThat(testB.getApplicationContext()).isNotNull();
	}

	@Test
	public void testC() {
		TestBean testBean = appContext.getBean("testCBean", TestBean.class);
		assertThat(testBean).isNotNull();
		assertThat(testBean.getApplicationContext()).isNotNull();
		assertThat(testBean.getName()).isEqualTo("C");
	}

	@Test
	public void testD() {
		Map<String, TestInjectionDBean> beans = appContext.getBeansOfType(TestInjectionDBean.class);

		TestInjectionDBean testInjectionBeanA = beans.get("testInjectionDBeanA");
		assertThat(testInjectionBeanA).isNotNull();
		assertThat(testInjectionBeanA.getTestBean()).isNotNull();
		assertThat(testInjectionBeanA.getTestBean().getName()).isEqualTo("A");

		TestInjectionDBean testInjectionBeanB = beans.get("testInjectionDBeanB");
		assertThat(testInjectionBeanB).isNotNull();
		assertThat(testInjectionBeanB.getTestBean()).isNotNull();
		assertThat(testInjectionBeanB.getTestBean().getName()).isEqualTo("B");
	}

	@Test
	public void testE() {
		Map<String, TestInjectionBean> beans = appContext.getBeansOfType(TestInjectionBean.class);

		TestInjectionBean testInjectionBeanA = beans.get("testInjectionBeanA");
		assertThat(testInjectionBeanA).isNotNull();
		assertThat(testInjectionBeanA.getTestBean()).isNotNull();
		assertThat(testInjectionBeanA.getTestBean().getName()).isEqualTo("A");

		TestInjectionBean testInjectionBeanB = beans.get("testInjectionBeanB");
		assertThat(testInjectionBeanB).isNotNull();
		assertThat(testInjectionBeanB.getTestBean()).isNotNull();
		assertThat(testInjectionBeanB.getTestBean().getName()).isEqualTo("A");

		TestInjectionBean testInjectionBeanC = beans.get("testInjectionBeanC");
		assertThat(testInjectionBeanC).isNotNull();
		assertThat(testInjectionBeanC.getTestBean()).isNotNull();
		assertThat(testInjectionBeanC.getTestBean().getName()).isEqualTo("B");
	}
}