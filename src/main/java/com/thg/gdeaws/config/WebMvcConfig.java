package com.thg.gdeaws.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.thg.gdeaws.setup.interception.AuthInterceptorAdapter;

@Configuration
@EnableAutoConfiguration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private LocaleChangeInterceptor localeChangeInterceptor;
	
	@Autowired
	private AuthInterceptorAdapter authInterceptorAdapter;
	
	
	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(true)
		.favorParameter(true)
		.parameterName("format")
		.ignoreAcceptHeader(true)
		.useJaf(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("html", MediaType.APPLICATION_XHTML_XML)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("json", MediaType.APPLICATION_JSON);
	}
	
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(authInterceptorAdapter);
    }
    
    @Bean
	public LocaleChangeInterceptor localeChangeInterceptor(){
		LocaleChangeInterceptor localeChangeInterceptor =
				new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	
	@Bean
	public ReloadableResourceBundleMessageSource messageSource(){
		ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource =
				new ReloadableResourceBundleMessageSource();
		reloadableResourceBundleMessageSource.setBasenames(new String[] {
				"classpath:list-errors",
				"classpath:list-validations"
		});
		return reloadableResourceBundleMessageSource;
	}
	
	@Bean 
	public CookieLocaleResolver localeResolver(){
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}
}