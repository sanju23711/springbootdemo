package com.thg.gdeaws.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

	String format;
	
	@Override
	public void initialize(ValidDate validDate) {
		format = validDate.format();
	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(str)) {
			return true;
		}
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.parse(str);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

}
