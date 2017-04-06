package com.thg.gdeaws.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thg.gdeaws.exception.ServiceNotAvailableException;
import com.thg.gdeaws.exchange.Request;
import com.thg.gdeaws.exchange.Response;

@Service("postUtil")
public class PostUtil {
	
	@Autowired
	private ConversionUtil conversionUtil;
	
	public <I, O> Response<O> post(String url, Class<O> clazz, Request<I> request) throws ServiceNotAvailableException {
		String requestString = conversionUtil.printToJson(request);
		String responseString = processHttpPost(url, requestString);
		return conversionUtil.parseResponseFromJson(clazz, responseString);
	}
	
	public static String processHttpPost(String url, String requestString) throws ServiceNotAvailableException {
		return processHttpPost(url, requestString, "application/json");
	}
	
	public static String processHttpPost(String url, String requestString, String mediaType) throws ServiceNotAvailableException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		Header accept = new Header("Accept", mediaType);
		post.addRequestHeader(accept);
		try {
			StringRequestEntity entity = new StringRequestEntity(requestString, mediaType, null);
			post.setRequestEntity(entity);
			client.executeMethod(post);
			String responseString = IOUtils.toString(post.getResponseBodyAsStream());
			return responseString;
		} catch (Exception e) {
			throw new ServiceNotAvailableException("Service('" + url + "') Not Available. Please try again later or contact your Hibbert Administrator");
		}
	}
}
