package com.thg.gdeaws.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.thg.gdeaws.setup.environment.Environment;

@Configuration
public class HibernateConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean(name="sessionFactory")
	public  SessionFactory sessionFactory(){
		return localSessionFactory().getObject();
	}
	
	@Bean
	public LocalSessionFactoryBean localSessionFactory(){
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setPackagesToScan("com.thg.gdeaws.db.model");
		sessionFactoryBean.setHibernateProperties(getHibernateProperties());
		sessionFactoryBean.setDataSource(dataSource());
		return sessionFactoryBean;
	}
	
	
	@Bean
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager hibernateTransactionManager =
				new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(localSessionFactory().getObject());
		return hibernateTransactionManager;
	}
	
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource()  {
		try{
			JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
			bean.setJndiName("java:comp/env/" + environment.property("jndi"));
			bean.setProxyInterface(DataSource.class);
			bean.setLookupOnStartup(false);
			bean.afterPropertiesSet();
			return (DataSource)bean.getObject();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private Properties getHibernateProperties(){
		Properties properties = new Properties();
		properties.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
		properties.setProperty("jdbc.batch_size", "<![CDATA[0]]>");
		properties.setProperty("hibernate.cache.use_query_cache", "false");
		properties.setProperty("hibernate.cache.use_second_level_cache", "false");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
		//properties.setProperty("hibernate.show_sql", "true");
		//properties.setProperty("hibernate.format_sql", "true");
		return properties;
	}
}
