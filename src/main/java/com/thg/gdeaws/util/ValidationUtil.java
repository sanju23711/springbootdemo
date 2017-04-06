package com.thg.gdeaws.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Service;

import com.thg.gdeaws.exception.ObjectValidationException;

@Service("validationUtil")
public class ValidationUtil {

	public <T> void rejectIfInValid(T t) throws ObjectValidationException {
		Map<String, String> errors = validate(t);
		if(errors.size() > 0)
			throw new ObjectValidationException(errors);
	}
	
	public <T> Map<String, String> validate(T t) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		
		Map<String, String> errors = new HashMap<String, String>();
		for (ConstraintViolation<T> violation : violations)
			errors.put(violation.getPropertyPath() + "("+ 
					violation.getInvalidValue() + ")",
					violation.getMessage());
		return errors;
	}
	
}
