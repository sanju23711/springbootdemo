package com.thg.gdeaws.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class OptionValidator implements ConstraintValidator<ValidOption, String> {

	String[] options;
	
	@Override
	public void initialize(ValidOption option) {
		options = option.options();
	}

	@Override
	public boolean isValid(String str, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(str)) 
			return true;
		for(String o: options)
			if(o.equals(str))
				return true;
		return false;
	}

}
