package com.thg.gdeaws.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

	@Override
	public void initialize(ValidNumber number) {

	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(str)) {
			return true;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

}
