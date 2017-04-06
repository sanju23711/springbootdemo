package com.thg.gdeaws.exchange;

import java.util.HashMap;
import java.util.Map;

public class RestError {
	private String errorCode;
	private String subCode;
	private String cause;
	private String description;
	private Map<String, String> validations = new HashMap<String, String>();

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getValidations() {
		return this.validations;
	}

	public void setValidations(Map<String, String> validations) {
		this.validations = validations;
	}

}