package com.thg.gdeaws.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class EndPoint {
	private String endpointName;
	private String title;
	private String path;
	private String acceptedMethods;
	private String acceptedFormat;
	private String outputFormat;
	private String description;
	private Request sampleRequest;
	private String sampleRequestUrl;
	private Response successResponse;
	private Response errorResponse;
	public String getEndpointName() {
		return this.endpointName;
	}
	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return this.path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAcceptedMethods() {
		return this.acceptedMethods;
	}
	public void setAcceptedMethods(String acceptedMethods) {
		this.acceptedMethods = acceptedMethods;
	}
	public String getAcceptedFormat() {
		return this.acceptedFormat;
	}
	public void setAcceptedFormat(String acceptedFormat) {
		this.acceptedFormat = acceptedFormat;
	}
	public String getOutputFormat() {
		return this.outputFormat;
	}
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Request getSampleRequest() {
		return this.sampleRequest;
	}
	public void setSampleRequest(Request sampleRequest) {
		this.sampleRequest = sampleRequest;
	}
	public String getSampleRequestUrl() {
		return this.sampleRequestUrl;
	}
	public void setSampleRequestUrl(String sampleRequestUrl) {
		this.sampleRequestUrl = sampleRequestUrl;
	}
	public Response getSuccessResponse() {
		return this.successResponse;
	}
	public void setSuccessResponse(Response successResponse) {
		this.successResponse = successResponse;
	}
	public Response getErrorResponse() {
		return this.errorResponse;
	}
	public void setErrorResponse(Response errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	public String convert(Object object){
		String converted = "";
		try {
			converted = 
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (Exception e) {}
		return converted;
	}
}
