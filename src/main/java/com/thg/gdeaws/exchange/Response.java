package com.thg.gdeaws.exchange;

public class Response<O> {
	
	private boolean success;
	private String description;
	private String url;
	private O output;
	private RestError error;
	
	public Response() {
		super();
	}
	
	public Response(boolean success, String description, String url, O output,
			RestError error) {
		super();
		this.success = success;
		this.description = description;
		this.url = url;
		this.output = output;
		this.error = error;
	}

	public boolean isSuccess() {
		return this.success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public O getOutput() {
		return this.output;
	}
	public void setOutput(O output) {
		this.output = output;
	}
	public RestError getError() {
		return this.error;
	}
	public void setError(RestError error) {
		this.error = error;
	}
}
