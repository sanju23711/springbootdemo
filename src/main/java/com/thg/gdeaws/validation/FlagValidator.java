package com.thg.gdeaws.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class FlagValidator implements ConstraintValidator<ValidFlag, String> {

	@Override
	public void initialize(ValidFlag number) {

	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(str)) 
			return false;
		if(str.length() == 1 && (str.equalsIgnoreCase("Y") || str.equalsIgnoreCase("N")))
			return true;
		return false;
	}

}
