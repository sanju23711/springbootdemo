package com.thg.gdeaws.setup.environment;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;

public class Environment {

	public static final String HOSTNAME = "machine.name";
	public static final String ENVIRONMENT = "environment";
	public static final String SITE_URL = "site.url";
	
	public final static Environment INSTANCE = new Environment();
	
	@Autowired
	private AbstractBeanFactory beanFactory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
    protected Environment() {
    	
    }
    
	public <T> T bean(Class<T> clazz, String name) {
		try{
			return applicationContext.getBean(clazz, name);
		}catch(NoSuchBeanDefinitionException e){}
		return null;
	}
	
	public ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public String property(String key) {
		try{
			return beanFactory.resolveEmbeddedValue("${" + key + "}");
		}catch(Exception exception){ return null; }
    }
}
