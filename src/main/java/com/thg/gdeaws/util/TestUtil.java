package com.thg.gdeaws.util;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.gdeaws.exchange.Request;
import com.thg.gdeaws.exchange.Response;

public class TestUtil {

	public static void main(String[] args) throws IOException {
		
		//login();
		validate();
	}
	
	@SuppressWarnings("rawtypes")
	private static void validate() {
		String url = "http://tcc-tcdev01:10088/v1/auth/validate/post";
		url = "http://localhost:8090/v1/auth/validate/post";
		//url = "http://localhost:8090/validate";
		Request request = new Request();
		
		request.setBadge("997624a09i0670a2a70i308i49a77021u01e6878");
		request.setAccount("INSMED");
		request.setUsername("mkandasamy");
		request.setPassword("Hibbert1");

		String data = getJsonData(request);
		String json = post(url, data);
		Response response = parseFromJson(json, Response.class);
		System.out.print(getJsonData(response));
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private static void login() {
		String url = "http://tcc-tcdev01:10088/v1/auth/login/post";
		//url = "http://localhost:8090/validate";
		Request request = new Request();
		
		//request.setBadge("997624a09i0670a2a70i308i49a77021u01e68781");
		request.setAccount("INSMED");
		request.setUsername("mkandasamy");
		request.setPassword("Hibbert1");

		String data = getJsonData(request);
		String json = post(url, data);
		Response response = parseFromJson(json, Response.class);
		System.out.print(getJsonData(response));
	}
	
	
	public static <T> T  parseFromJson(String json, Class<T> clazz){
		ObjectMapper objectMapper = new ObjectMapper();
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
	
	
	
	private static String getJsonData(Object object){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}
	
	
	public static String post(String url, String requestString) {
		try {
			return post(url, requestString, "application/json");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String post(String url, String requestString, String mediaType) throws IOException{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		Header accept = new Header("Accept", mediaType);
		post.addRequestHeader(accept);
		StringRequestEntity entity = new StringRequestEntity(requestString, mediaType, null);
		post.setRequestEntity(entity);
		client.executeMethod(post);
		String responseString = IOUtils.toString(post.getResponseBodyAsStream());
		return responseString;
	}

}
