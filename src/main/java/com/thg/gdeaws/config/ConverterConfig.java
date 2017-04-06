package com.thg.gdeaws.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.castor.CastorMarshaller;

@Configuration
public class ConverterConfig {
	
	@Autowired
	private CastorMarshaller castorMarshaller;
	
	@Bean
	public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter(){
		MappingJackson2HttpMessageConverter jacksonHttpMessageConverter =
				new MappingJackson2HttpMessageConverter();
		jacksonHttpMessageConverter.setPrettyPrint(true);
		return jacksonHttpMessageConverter;
	}
	
	@Bean
	public MarshallingHttpMessageConverter xmlHttpMessageConverter(){
		MarshallingHttpMessageConverter xmlHttpMessageConverter =
				new MarshallingHttpMessageConverter();
		xmlHttpMessageConverter.setMarshaller(castorMarshaller);
		xmlHttpMessageConverter.setUnmarshaller(castorMarshaller);
		return xmlHttpMessageConverter;
	}
	
	@SuppressWarnings("serial")
	@Bean
	public static CastorMarshaller castorMarshaller(){
		CastorMarshaller castorMarshaller = new CastorMarshaller();
		castorMarshaller.setCastorProperties(
				new HashMap<String, String>() {{
					put("org.exolab.castor.xml.naming", "mixed");
					put("org.exolab.castor.indent", "true");
				}}
		);
		castorMarshaller.setSuppressNamespaces(true);
		castorMarshaller.setSuppressXsiType(true);
		return castorMarshaller;
	}
	
	
}
