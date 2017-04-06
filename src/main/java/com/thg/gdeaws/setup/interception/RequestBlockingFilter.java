package com.thg.gdeaws.setup.interception;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RequestBlockingFilter implements Filter {

	static final Logger reportsLogger = Logger.getLogger("reportsLogger");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
				throws IOException, ServletException {
		try {
			((HttpServletResponse)response).sendRedirect("/blocked");
	        chain.doFilter (request, response);   
		} catch(Exception e) {
			reportsLogger.error(e, e);
		}
	}

	@Override
	public void destroy() {
		
	}
}