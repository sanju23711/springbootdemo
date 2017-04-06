package com.thg.gdeaws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.thg.gdeaws.exchange.EndPoint;
import com.thg.gdeaws.exchange.LoginOutput;
import com.thg.gdeaws.exchange.Request;
import com.thg.gdeaws.exchange.Response;
import com.thg.gdeaws.exchange.RestError;
import com.thg.gdeaws.setup.environment.Environment;
import com.thg.gdeaws.util.ConversionUtil;

@Service("apiService")
public class ApiService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ConversionUtil conversionUtil;
	
	public List<EndPoint> getEndpoints(){
		List<EndPoint> endPoints = getLoginEndpoints();
		endPoints.addAll(getLoginValidationEndpoints());
		return endPoints;
	}
	
	public List<EndPoint> getLoginValidationEndpoints(){
		List<EndPoint> endPoints = new ArrayList<EndPoint>();
		endPoints.add(getLoginValidationEndpointGET());
		endPoints.add(getLoginValidationEndpointPOST());
		return endPoints;
	}
	
	public List<EndPoint> getLoginEndpoints(){
		List<EndPoint> endPoints = new ArrayList<EndPoint>();
		endPoints.add(getLoginEndpointGET());
		endPoints.add(getLoginEndpointPOST());
		return endPoints;
	}
	
	public EndPoint getLoginValidationEndpointGET(){
		EndPoint endPoint = getLoginEndpoint();
		endPoint.setEndpointName("login validate get");
		endPoint.setTitle("Data Entry Login Validation With GET Request");
		endPoint.setPath("/v1/auth/validate/get.json");
		endPoint.setAcceptedMethods("GET");
		endPoint.setDescription("");
		endPoint.setSampleRequestUrl(Environment.INSTANCE.property(Environment.SITE_URL)
				+ "/v1/auth/validate/get.json?badge=997624a09i0670a2a70i308i49a77021u01e6878&account=SAMPLE&username=sampleusername&password=samplepassword");
		endPoint.getSuccessResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/validate/get.json");
		endPoint.getErrorResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/validate/get.json");
		return endPoint;
	}
	
	public EndPoint getLoginEndpointGET(){
		EndPoint endPoint = getLoginEndpoint();
		endPoint.setEndpointName("login get");
		endPoint.setTitle("Data Entry Login With GET Request");
		endPoint.setPath("/v1/auth/login/get.json");
		endPoint.setAcceptedMethods("GET");
		endPoint.setDescription("");
		endPoint.setSampleRequestUrl(Environment.INSTANCE.property(Environment.SITE_URL)
				+ "/v1/auth/login/get.json?account=SAMPLE&username=sampleusername&password=samplepassword");
		endPoint.getSuccessResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/login/get.json");
		endPoint.getErrorResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/login/get.json");
		return endPoint;
	}
	
	@SuppressWarnings("rawtypes")
	public EndPoint getLoginEndpointPOST(){
		EndPoint endPoint = getLoginEndpoint();
		endPoint.setEndpointName("login post");
		endPoint.setTitle("Data Entry Login With POST Request");
		endPoint.setPath("/v1/auth/login/post.json");
		endPoint.setAcceptedMethods("POST");
		endPoint.setDescription("");
		
		Request sampleRequest = new Request();
		sampleRequest.setAccount("SAMPLE");
		sampleRequest.setUsername("sampleusername");
		sampleRequest.setPassword("samplepassword");
		endPoint.setSampleRequest(sampleRequest);
		
		endPoint.getSuccessResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/login/post.json");
		endPoint.getErrorResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/login/post.json");
		return endPoint;
	}
	
	@SuppressWarnings("rawtypes")
	public EndPoint getLoginValidationEndpointPOST(){
		EndPoint endPoint = getLoginEndpoint();
		endPoint.setEndpointName("login validate post");
		endPoint.setTitle("Data Entry Login Validate With POST Request");
		endPoint.setPath("/v1/auth/validate/post.json");
		endPoint.setAcceptedMethods("POST");
		endPoint.setDescription("");
		
		Request sampleRequest = new Request();
		sampleRequest.setBadge("997624a09i0670a2a70i308i49a77021u01e6878");
		sampleRequest.setAccount("SAMPLE");
		sampleRequest.setUsername("sampleusername");
		sampleRequest.setPassword("samplepassword");
		endPoint.setSampleRequest(sampleRequest);
		
		endPoint.getSuccessResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/validate/post.json");
		endPoint.getErrorResponse().setUrl(
				Environment.INSTANCE.property(Environment.SITE_URL) + "/v1/auth/validate/post.json");
		return endPoint;
	}
	
	public EndPoint getLoginEndpoint(){
		EndPoint endPoint = new EndPoint();
		endPoint.setAcceptedFormat("json, xml");
		endPoint.setOutputFormat("json, xml");
		endPoint.setSuccessResponse(getSuccessLoginResponse());
		endPoint.setErrorResponse(getErrorLoginResponse());
		return endPoint;
	}
	
	private Response<LoginOutput> getSuccessLoginResponse(){
		Response<LoginOutput> successResponse = new Response<LoginOutput>();
		successResponse.setSuccess(true);
		successResponse.setDescription("Login is successful");
		LoginOutput loginOutput = new LoginOutput();
		loginOutput.setAccount("SAMPLE");
		loginOutput.setUserId(12345L);
		loginOutput.setBadge("997624a09i0670a2a70i308i49a77021u01e6878");
		loginOutput.setValid(true);
		loginOutput.setLastLoggedOn(new Date());
		loginOutput.setBadgeExpiresOn(DateUtils.addDays(new Date(), 1));
		successResponse.setOutput(loginOutput);
		return successResponse;
	}
	
	private Response<LoginOutput> getErrorLoginResponse(){
		Response<LoginOutput> errorResponse = new Response<LoginOutput>();
		errorResponse.setSuccess(false);
		errorResponse.setDescription("Processing request is failed");
		errorResponse.setError(getSampleError());
		return errorResponse;
	}
	
	private RestError getSampleError(){
		RestError restError = new RestError();
		restError.setErrorCode("404");
		restError.setSubCode("404-001");
		restError.setCause("Not Found");
		restError.setDescription(Environment.INSTANCE.property(Environment.SITE_URL) + "/errors#404-001");
		restError.setValidations(getSampleValidations());
		return restError;
	}
	
	private Map<String, String> getSampleValidations(){
		Map<String, String> validations = new LinkedHashMap<String, String>();
		validations.put("1.1.1", messageSource.getMessage("1.1.1", null, Locale.US));
		validations.put("1.1.2", messageSource.getMessage("1.1.2", null, Locale.US));
		validations.put("1.1.3", messageSource.getMessage("1.1.2", null, Locale.US));
		validations.put("1.1.4", messageSource.getMessage("1.1.4", null, Locale.US));
		validations.put("1.1.5", messageSource.getMessage("1.1.5", null, Locale.US));
		validations.put("1.1.6", messageSource.getMessage("1.1.6", null, Locale.US));
		validations.put("2.0.1.1", "'account' is not valid");
		validations.put("2.0.1.2", "'account' is not in valid length");
		validations.put("2.0.1.3", "'account' is not found");
		validations.put("2.0.1.4", "'account' is not active");
		validations.put("2.0.2.1", "'credentials' are not valid");
		validations.put("2.0.2.2", "'credentials' are not valid. Please contact your Hibbert Administrator");
		validations.put("2.0.2.3", "'username' is not active. Please contact Hibbert Administrator");
		validations.put("2.0.3.1", "'account' is not in valid length");
		validations.put("2.0.3.1", "'badge' is invalid or expired. Please try again with correct badge and/or credentials");
		return validations;
	}

}
