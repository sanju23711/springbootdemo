package com.thg.gdeaws.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class StringLengthValidator implements ConstraintValidator<ValidLength, String> {

	int min;
	int max;
	
	@Override
	public void initialize(ValidLength length) {
		min = length.min();
		max = length.max();
	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(str)) {
			return true;
		}
		if(str.length() > max || str.length() < min)
			return false;
		return true;
	}

}
