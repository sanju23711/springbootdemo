package com.thg.gdeaws.setup.environment;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author mohan kandasamy
 */

@Configuration
public class EnvironmentPropertyPlaceholder extends PropertyPlaceholderConfigurer {
	
	private static final Logger LOG = Logger.getLogger(EnvironmentPropertyPlaceholder.class);
	private static Properties hosts = new Properties();
	static {
		InputStream inputStream = EnvironmentPropertyPlaceholder.class.getClassLoader().getResourceAsStream("list-environments.properties");
		if(inputStream != null)
			try {
				hosts.load(inputStream);
			} catch (IOException e) {
				java.util.logging.Logger.getLogger("EnvironmentPropertyPlaceholder.class").log(java.util.logging.Level.WARNING, "'list-environments.properties' is not found. 'LOCAL' is set as environment");
			}
	}
	
	@Override
	protected String resolvePlaceholder(String placeholder, Properties properties) {
		if (placeholder.startsWith("DEFAULT.")) 
            return properties.getProperty(placeholder);
        return defaultValue(
        		placeholder, properties.getProperty(placeholder), 
        		properties.getProperty("DEFAULT." + placeholder)
    		);
    }

    protected <T> T defaultValue(String placeholder, T t, T def) {
        if (t == null) {
            if (LOG.isDebugEnabled()) 
                LOG.warn("Value for place holder " + placeholder + " is missing; using default: " + def);
            return def;
        }

        if (LOG.isDebugEnabled()) 
            LOG.debug("Object has value; using: " + t);
        return t;
    }
    
    @Override
    protected void loadProperties(final Properties props) throws IOException {
    	String environment = "LOCAL"; 
        try {
        	String hostname = InetAddress.getLocalHost().getHostName().toUpperCase();
            if(hosts.containsKey(hostname))
            	environment = hosts.getProperty(hostname);
            else
            	environment = "LOCAL";
            if(environment.equalsIgnoreCase("local"))
            	environment = System.getProperty("catalina.base") == null ? "BOOT" : "LOCAL";
            props.setProperty("DEFAULT.machine.name", hostname);
	        props.setProperty("machine.name", hostname);
	        props.setProperty("environment", environment);
        	setLocations(getHostSpecificResources(environment));
	        super.loadProperties(props);
        } catch (UnknownHostException uhe) {
            LOG.error("Error getting hostname!", uhe);
        }
    }
    
    private Resource[] getHostSpecificResources(String environment){
    	Resource[] defaultResources = getAllAvailableResources("DEFAULT");
    	Resource[] hostSpecificResources = getAllAvailableResources(environment);
		Resource[] resources = new Resource[defaultResources.length + hostSpecificResources.length];
		System.arraycopy(defaultResources, 0, resources, 0, defaultResources.length);
      	System.arraycopy(hostSpecificResources, 0, resources, defaultResources.length, hostSpecificResources.length);
      	return resources;
    }
    
    private Resource[] getAllAvailableResources(String environment){
    	try {
    		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			return resolver.getResources("classpath:environment-" + environment + ".properties");
		} catch (IOException e) {
			LOG.error("Error getting property resources!", e);
			return new Resource[]{};
		}
    }
    
}
