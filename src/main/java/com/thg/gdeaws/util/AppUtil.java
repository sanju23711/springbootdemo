package com.thg.gdeaws.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mohan Kandasamy
 *
 */
public class AppUtil {
	
	
	public static String getRequestUrl(HttpServletRequest request) {
		return getBase(request) + request.getRequestURI();
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
		return getBase(request);
	}

	public static String getBase(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort();
	}
}
