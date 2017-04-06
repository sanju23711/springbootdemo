package com.thg.gdeaws.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy=OptionValidator.class)
public @interface ValidOption {
 
	String message();
	
	String[] options();

    @SuppressWarnings("rawtypes")
	Class[] groups() default {};
    
	@SuppressWarnings("rawtypes")
    Class[] payload() default {};
}
