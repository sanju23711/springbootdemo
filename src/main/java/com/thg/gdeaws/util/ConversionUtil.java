package com.thg.gdeaws.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.gdeaws.exchange.Response;

@Service("converter")
public class ConversionUtil {
	
	@Autowired
	private CastorMarshaller castorMarshaller;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String prettyPrintJson(String json){
		try {
			Object jsonObject = objectMapper.readValue(json, Object.class);
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		} catch (Exception e) {
			return json;
		}
	}
	

	public String print(HttpServletRequest httpServletRequest, Object object){
		return print(httpServletRequest.getRequestURI(), object);
	}
	
	public String print(String meadiaType, Object object){
		if(meadiaType.toLowerCase().endsWith(".xml"))
			return printToXml(object);
		else
			return printToJson(object);
	}
	
	public String printToJson(Object object){
		if(object == null)
			return "";
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String printToXml(Object object){
		try {
			StringWriter writer = new StringWriter();
			castorMarshaller.marshal(object, new StreamResult(writer));
			return writer.toString();
		} catch (XmlMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object parse(String meadiaType, String content, Class<?> clazz){
		if(meadiaType.toLowerCase().endsWith(".xml"))
			return parseFromXml(content);
		else
			return parseFromJson(content, clazz);
	}
	
	public <T> Response<T> parseResponseFromJson(Class<T> clazz, String jsonString) {
		try {
			return objectMapper.readValue(jsonString, new TypeReference<Response<T>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object parseFromJson(String json, Class<?> clazz){
		
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object parseFromXml(String xml){
		if(StringUtils.isEmpty(xml))
			return null;
		try {
			return castorMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
		} catch (XmlMappingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void prepare(CastorMarshaller castorMarshaller, ObjectMapper objectMapper){
		this.objectMapper = objectMapper;
		this.castorMarshaller = castorMarshaller;
	}
}