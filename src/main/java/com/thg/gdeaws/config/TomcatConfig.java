package com.thg.gdeaws.config;

import javax.sql.DataSource;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thg.gdeaws.setup.environment.Environment;

@Configuration
public class TomcatConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
		return new TomcatEmbeddedServletContainerFactory() {

			@Override
			protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
					Tomcat tomcat) {
				tomcat.enableNaming();
	            TomcatEmbeddedServletContainer container = 
	                    super.getTomcatEmbeddedServletContainer(tomcat);
	            for (Container child: container.getTomcat().getHost().findChildren()) {
	                if (child instanceof Context) {
	                    ClassLoader contextClassLoader = 
	                            ((Context)child).getLoader().getClassLoader();
	                    Thread.currentThread().setContextClassLoader(contextClassLoader);
	                    break;
	                }
	            }
	            return container;
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName(environment.property("jndi"));
				resource.setType(DataSource.class.getName());
				resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
				resource.setProperty("url", "jdbc:oracle:thin:@hgsun69:1539:repdu");
				resource.setProperty("username", "HGAPPTHG");
				resource.setProperty("password", "THGAPP");
				context.getNamingResources().addResource(resource);
			}
		};
	}
}
