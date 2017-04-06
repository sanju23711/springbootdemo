package com.thg.gdeaws.setup.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentLoaderListener 
        implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private EnvironmentLoader environmentLoader;
  
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	environmentLoader.load();
    }
}