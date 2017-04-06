package com.thg.gdeaws.exception;

import com.thg.gdeaws.exchange.LoginOutput;
import com.thg.gdeaws.exchange.Response;

@SuppressWarnings("serial")
public class AuthenticationFailedException extends Exception {
	
	private Response<LoginOutput> response;

	public AuthenticationFailedException() {
		super();
	}
	
	public AuthenticationFailedException(Response<LoginOutput> response) {
		super();
		this.response = response;
		
	}
	
	public AuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

	public Response<LoginOutput> getResponse() {
		return this.response;
	}

	public void setResponse(Response<LoginOutput> response) {
		this.response = response;
	}
}

