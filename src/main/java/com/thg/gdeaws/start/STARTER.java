package com.thg.gdeaws.start;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.thg")
public class STARTER extends SpringBootServletInitializer {
	
	static final Logger LOG = Logger.getLogger(STARTER.class);
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class<?>[] {STARTER.class});
    }

	public static void main(String[] args) {
		SpringApplication.run(STARTER.class, args);
		System.out.println(System.getProperty("catalina.base"));
	}
}
