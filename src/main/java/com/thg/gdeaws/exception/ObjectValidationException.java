package com.thg.gdeaws.exception;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class ObjectValidationException extends Exception {
	
	private Map<String, String> validations = new HashMap<String, String>();

	public ObjectValidationException() {
		super();
	}
	
	public ObjectValidationException(String key, String message) {
		super();
		this.validations.put(key, message);
	}
	
	public ObjectValidationException(Map<String, String> validations) {
		super();
		this.validations = validations;
	}

	public ObjectValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectValidationException(String message) {
		super(message);
	}

	public ObjectValidationException(Throwable cause) {
		super(cause);
	}

	public Map<String, String> getValidations() {
		return this.validations;
	}

	public void setValidations(Map<String, String> validations) {
		this.validations = validations;
	}
}
