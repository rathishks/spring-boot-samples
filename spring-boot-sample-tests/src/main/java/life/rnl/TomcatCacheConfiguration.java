package life.rnl;

import javax.servlet.Servlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(EmbeddedServletContainerAutoConfiguration.class)
@ConditionalOnWebApplication
public class TomcatCacheConfiguration {

	@ConditionalOnClass({ Servlet.class, Tomcat.class })
	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				final int cacheSize = 40 * 1024;
				StandardRoot standardRoot = new StandardRoot(context);
				standardRoot.setCacheMaxSize(cacheSize);
				context.setResources(standardRoot);
			}
		};
		return tomcatFactory;
	}
}