package com.thg.gdeaws.exception;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "Exception")
public class ApplicationException extends Exception {

	public ApplicationException() {
		
	}
	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	
}
