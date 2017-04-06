package com.thg.gdeaws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.thg.gdeaws.exception.ApplicationException;
import com.thg.gdeaws.exception.AuthenticationFailedException;
import com.thg.gdeaws.exception.ObjectValidationException;
import com.thg.gdeaws.exchange.RestError;
import com.thg.gdeaws.util.AppUtil;
import com.thg.gdeaws.util.ErrorListUtil;

@Service(value="exceptionService")
public class ExceptionService {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ExceptionService.class);
	
	@Autowired 
	private HttpServletRequest httpServletRequest;
	
	public RestError parse(Exception ex){
		com.thg.gdeaws.exchange.RestError error = null;
		if(ex instanceof ApplicationException)
			error = parse(HttpStatus.BAD_REQUEST, "002", (ApplicationException)ex);
		else if(ex instanceof ObjectValidationException)
			error =  parse(HttpStatus.BAD_REQUEST, "003", (ObjectValidationException)ex);
		else if(ex instanceof MissingServletRequestPartException)
			error =  parse(HttpStatus.BAD_REQUEST, "004", (MissingServletRequestPartException)ex);
		else if(ex instanceof MissingServletRequestParameterException)
			error = parse(HttpStatus.BAD_REQUEST, "005", (MissingServletRequestParameterException)ex);
		else if(ex instanceof HttpMessageNotReadableException)
			error = parse(HttpStatus.BAD_REQUEST, "006", (HttpMessageNotReadableException)ex);
		else if(ex instanceof TypeMismatchException)
			error = parse(HttpStatus.BAD_REQUEST, "007", (TypeMismatchException)ex);
		else if(ex instanceof MethodArgumentNotValidException)
			error = parse(HttpStatus.BAD_REQUEST, "008", (MethodArgumentNotValidException)ex);
		else if(ex instanceof BindException)
			error = parse(HttpStatus.BAD_REQUEST, "009", (BindException)ex);
		else if(ex instanceof AuthenticationFailedException)
			error =  parse(HttpStatus.BAD_REQUEST, "010", (AuthenticationFailedException)ex);
		else if(ex instanceof GenericJDBCException)
			error =  parse(HttpStatus.INTERNAL_SERVER_ERROR, "002", (GenericJDBCException)ex);
		else if(ex instanceof HibernateException)
			error =  parse(HttpStatus.INTERNAL_SERVER_ERROR, "002", (HibernateException)ex);
		else if(ex instanceof HttpMessageNotWritableException)
			error =  parse(HttpStatus.INTERNAL_SERVER_ERROR, "003", (HttpMessageNotWritableException)ex);
		else if(ex instanceof ConversionNotSupportedException)
			error = parse(HttpStatus.INTERNAL_SERVER_ERROR, "004", (ConversionNotSupportedException)ex);
		else if(ex instanceof HttpMessageConversionException)
			error = parse(HttpStatus.INTERNAL_SERVER_ERROR, "005", (HttpMessageConversionException)ex);
		else if(ex instanceof HttpMediaTypeNotSupportedException)
			error = parse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "001", (HttpMediaTypeNotSupportedException)ex);
		else if(ex instanceof HttpMediaTypeNotAcceptableException)
			error = parse(HttpStatus.NOT_ACCEPTABLE, "001", (HttpMediaTypeNotAcceptableException)ex);
		else if(ex instanceof ResourceAccessException)
			error = parse(HttpStatus.NOT_FOUND, "001", (ResourceAccessException)ex);
		else if(ex instanceof NoSuchRequestHandlingMethodException)
			error =  parse(HttpStatus.NOT_FOUND, "002", (NoSuchRequestHandlingMethodException)ex);
		else if(ex instanceof HttpRequestMethodNotSupportedException)
			error = parse(HttpStatus.METHOD_NOT_ALLOWED, "001", (HttpRequestMethodNotSupportedException)ex);
		else
			error = parse(HttpStatus.INTERNAL_SERVER_ERROR, "099", ex);
		error.setDescription(AppUtil.getBase(httpServletRequest) + "/errors#" + error.getSubCode());
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, ObjectValidationException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		error.setValidations(ex.getValidations());
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, Exception ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.1", "Generic Error: " + ex.getMessage());
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, MissingServletRequestParameterException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.2", "Param Missing: " +  ex.getParameterName() + "-" +  ex.getMessage() + " with data type java:" + ex.getParameterType());
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, TypeMismatchException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.3", "Param Type Mismatch: " + ex.getMessage() + "; required type java:" + ex.getRequiredType().getName());
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, HttpRequestMethodNotSupportedException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.4", "Request Method Not Supported: '" + ex.getMethod() + "' is not supported by this url. " + "; supported methods" + StringUtils.join(ex.getSupportedHttpMethods().iterator(), ","));
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, MethodArgumentNotValidException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		List<String> errors = new ArrayList<String>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) 
			errors.add(fieldError.getField() + "-" + fieldError.getDefaultMessage());
		for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) 
			errors.add(objectError.getObjectName() + "-" + objectError.getDefaultMessage());
		validations.put("1.1.5", "Method Args Validation: " + StringUtils.join(errors, ","));
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, GenericJDBCException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.6", "Internal Server Exception: Please contact your Hibbert Administrator");
		error.setValidations(validations);
		return error;
	}
	
	private RestError parse(HttpStatus status, String subCode, HibernateException ex){
		RestError error = ErrorListUtil.getError(status, subCode);
		Map<String, String> validations = new HashMap<String, String>();
		validations.put("1.1.6", "Internal Server Exception: Please contact your Hibbert Administrator");
		error.setValidations(validations);
		return error;
	}
}
