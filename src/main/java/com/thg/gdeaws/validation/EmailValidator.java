package com.thg.gdeaws.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Anuj
 * 
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

	private static String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	Pattern pattern;
	Matcher matcher;

	@Override
	public void initialize(ValidEmail email) {
		pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if(StringUtils.isEmpty(email))
			return true;
		
		CharSequence emailstr = email;
		matcher = pattern.matcher(emailstr);

		return matcher.matches();
	}

}