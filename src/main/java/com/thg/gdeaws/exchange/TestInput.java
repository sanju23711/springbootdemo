package com.thg.gdeaws.exchange;

import org.hibernate.validator.constraints.NotEmpty;

import com.thg.gdeaws.validation.ValidLength;
import com.thg.gdeaws.validation.ValidNumber;

public class TestInput {
	
	@NotEmpty(message="100.1.1 'alpha' is required")
	@ValidLength(min=1, max=20, message="100.1.2 Incorrect length for 'alpha'")
	private String alpha;
	
	@NotEmpty(message="100.2.1 'beta' is Required")
	@ValidLength(min=1, max=5, message="100.2.2 Incorrect length for 'beta'")
	@ValidNumber(message="100.2.3 'beta' should be a valid number")
	private String beta;
	
	public String getAlpha() {
		return this.alpha;
	}
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	public String getBeta() {
		return this.beta;
	}
	public void setBeta(String beta) {
		this.beta = beta;
	}
	
	
}
