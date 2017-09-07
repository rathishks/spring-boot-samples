package life.rnl.autowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class AutowiringScenarios {

	public class TestBean {
		private ApplicationContext applicationContext;
		private String name;

		public TestBean(ApplicationContext applicationContext, String name) {
			this.applicationContext = applicationContext;
			this.name = name;
		}

		public ApplicationContext getApplicationContext() {
			return this.applicationContext;
		}

		public String getName() {
			return this.name;
		}
	}

	public class TestInjectionBean {
		private TestBean testBean;

		public TestInjectionBean(TestBean testBean) {
			this.testBean = testBean;
		}

		public TestBean getTestBean() {
			return this.testBean;
		}
	}

	/**
	 * The purpose of this test is to show the injection functionality against
	 * the class constructor.
	 * 
	 * <br />
	 * <br />
	 * 
	 * See {@link AutowiringScenariosTests#testA()}
	 */
	@Component
	public class TestA {
		private ApplicationContext applicationContext;

		@Autowired
		public TestA(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		public ApplicationContext getApplicationContext() {
			return this.applicationContext;
		}
	}

	/**
	 * The purpose of this test is to show that the {@link @Autowired}
	 * annotation is optional (see {@link TestA}).
	 * 
	 * <br />
	 * <br />
	 * 
	 * See {@link AutowiringScenariosTests#testB()}
	 */
	@Component
	public class TestB {
		private ApplicationContext applicationContext;

		public TestB(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		public ApplicationContext getApplicationContext() {
			return this.applicationContext;
		}
	}

	/**
	 * The purpose of this test is to show that you can instantiate the class
	 * with injected parameters from a {@link @Bean} annotated method.
	 * 
	 * <br />
	 * <br />
	 * 
	 * See {@link AutowiringScenariosTests#testC()}
	 */
	public class TestC {
		@Bean
		public TestBean testCBean(ApplicationContext applicationContext) {
			return new TestBean(applicationContext, "C");
		}
	}

	/**
	 * The purpose of this test is to show that if no {@link @Bean} is marked as
	 * primary, you can name the parameter the same name as the {@link @Bean} to
	 * resolve the correct bean (as opposed to putting a direct
	 * {@link @Qualifier} on the parameter).
	 * 
	 * <br />
	 * <br />
	 * 
	 * See {@link AutowiringScenariosTests#testD()}
	 */
	public class TestD {
		public class TestDBean {
			private ApplicationContext applicationContext;
			private String name;

			public TestDBean(ApplicationContext applicationContext, String name) {
				this.applicationContext = applicationContext;
				this.name = name;
			}

			public ApplicationContext getApplicationContext() {
				return this.applicationContext;
			}

			public String getName() {
				return this.name;
			}
		}

		public class TestInjectionDBean {
			private TestDBean testBean;

			public TestInjectionDBean(TestDBean testBean) {
				this.testBean = testBean;
			}

			public TestDBean getTestBean() {
				return this.testBean;
			}
		}

		@Bean
		public TestDBean testBeanA(ApplicationContext applicationContext) {
			return new TestDBean(applicationContext, "A");
		}

		@Bean
		public TestDBean testBeanB(ApplicationContext applicationContext) {
			return new TestDBean(applicationContext, "B");
		}

		@Bean
		public TestInjectionDBean testInjectionDBeanA(TestDBean testBeanA) {
			return new TestInjectionDBean(testBeanA);
		}

		@Bean
		public TestInjectionDBean testInjectionDBeanB(TestDBean testBeanB) {
			return new TestInjectionDBean(testBeanB);
		}
	}

	/**
	 * The purpose of this test is to show that if a {@link @Bean} is marked as
	 * primary, you are forced to use a {@link @Qualifier} in order to resolve
	 * the correct bean.
	 * 
	 * <br />
	 * <br />
	 * 
	 * <strong>testInjectionBeanB</strong> will resolve with
	 * <strong>testEBeanA</strong> in this scenario.
	 * 
	 * <br />
	 * <br />
	 * 
	 * See {@link AutowiringScenariosTests#testE()}
	 */
	public class TestE {
		@Bean
		@Primary
		public TestBean testEBeanA(ApplicationContext applicationContext) {
			return new TestBean(applicationContext, "A");
		}

		@Bean
		public TestBean testEBeanB(ApplicationContext applicationContext) {
			return new TestBean(applicationContext, "B");
		}

		@Bean
		public TestInjectionBean testInjectionBeanA(TestBean testEBeanA) {
			return new TestInjectionBean(testEBeanA);
		}

		@Bean
		public TestInjectionBean testInjectionBeanB(TestBean testEBeanB) {
			return new TestInjectionBean(testEBeanB);
		}

		@Bean
		public TestInjectionBean testInjectionBeanC(@Qualifier("testEBeanB") TestBean testEBeanB) {
			return new TestInjectionBean(testEBeanB);
		}
	}
}