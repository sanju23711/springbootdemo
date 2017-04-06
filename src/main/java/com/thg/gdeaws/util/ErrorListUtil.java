package com.thg.gdeaws.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.thg.gdeaws.exchange.RestError;
import com.thg.gdeaws.setup.environment.Environment;

public class ErrorListUtil {
	
	public static List<RestError> getErrorList(){
		List<RestError> list = new ArrayList<RestError>();
		list.add(getError(HttpStatus.BAD_REQUEST, "002"));
		list.add(getError(HttpStatus.BAD_REQUEST, "003"));
		list.add(getError(HttpStatus.BAD_REQUEST, "004"));
		list.add(getError(HttpStatus.BAD_REQUEST, "005"));
		list.add(getError(HttpStatus.BAD_REQUEST, "006"));
		list.add(getError(HttpStatus.BAD_REQUEST, "007"));
		list.add(getError(HttpStatus.BAD_REQUEST, "008"));
		list.add(getError(HttpStatus.BAD_REQUEST, "009"));
		list.add(getError(HttpStatus.BAD_REQUEST, "010"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "001"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "002"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "003"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "004"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "005"));
		list.add(getError(HttpStatus.INTERNAL_SERVER_ERROR, "099"));
		list.add(getError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "001"));
		list.add(getError(HttpStatus.NOT_ACCEPTABLE, "001"));
		list.add(getError(HttpStatus.NOT_FOUND, "001"));
		list.add(getError(HttpStatus.NOT_FOUND, "002"));
		list.add(getError(HttpStatus.METHOD_NOT_ALLOWED, "001"));
		return list;
	}
	
	public static com.thg.gdeaws.exchange.RestError getError(HttpStatus status, String subCode){
		RestError error = new RestError();
		error.setErrorCode(status.value() + "");
		error.setSubCode(status.value() + "-" + subCode);
		error.setCause(status.getReasonPhrase());
		error.setDescription(getInfo(status.value() + "-" + subCode));
		return error;
	}
	
	public static String getInfo(String code) {
		return Environment.INSTANCE.property(Environment.SITE_URL) + "/errors#" + code;
	}
}
