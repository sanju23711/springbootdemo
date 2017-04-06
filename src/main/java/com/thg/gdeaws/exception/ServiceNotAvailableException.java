package com.thg.gdeaws.exception;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class ServiceNotAvailableException extends Exception {
	
	private Map<String, String> messages = new HashMap<String, String>();

	public ServiceNotAvailableException() {
		super();
	}
	
	public ServiceNotAvailableException(String key, String message) {
		super();
		this.messages.put(key, message);
	}
	
	public ServiceNotAvailableException(Map<String, String> messages) {
		super();
		this.messages = messages;
	}
	
	public ServiceNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceNotAvailableException(String message) {
		super(message);
	}

	public ServiceNotAvailableException(Throwable cause) {
		super(cause);
	}

	public Map<String, String> getMessages() {
		return this.messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String key, String message) {
		this.messages.put(key, message);
	}

}

