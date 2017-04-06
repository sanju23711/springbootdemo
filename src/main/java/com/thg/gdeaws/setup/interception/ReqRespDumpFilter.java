package com.thg.gdeaws.setup.interception;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thg.gdeaws.setup.environment.Environment;
import com.thg.gdeaws.util.AppUtil;
import com.thg.gdeaws.util.ConversionUtil;


@Component
public class ReqRespDumpFilter implements Filter {

	private static final Logger DUMP_LOG = Logger.getLogger("dumpLogger");
	private static Logger LOG = Logger.getLogger(ReqRespDumpFilter.class);
	
	@Autowired
	private ConversionUtil conversionUtil;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
				throws IOException, ServletException {
		try {
			httpServletRequest = (HttpServletRequest)servletRequest;
			httpServletResponse = (HttpServletResponse)servletResponse;
			
	        BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);  
	        BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);
	        String content = bufferedReqest.getRequestBody();
	        httpServletRequest.setAttribute("input", content);
		        
	        getLogger().info("******************** Start **************************");
	        printMetadata(httpServletRequest);
	        if(!httpServletRequest.getRequestURI().contains("favicon.ico"))
	        	printRequestData(content);
	        
	        chain.doFilter (bufferedReqest, bufferedResponse);  
	        
	        if(!httpServletRequest.getRequestURI().contains("favicon.ico"))
	        	content = bufferedResponse.getContent();
	        if(content != null) printResponseData(content);
	        getLogger().info("*********************** End **************************\n\n\n");
		} catch( Throwable a ) {
			getLogger().error(a);
		}
	}
	
	private void printMetadata(HttpServletRequest httpServletRequest){
		Map<String, String> requestMap = getTypesafeRequestMap(httpServletRequest);
		Map<String, Object> metaData = new LinkedHashMap<String, Object>();
        metaData.put("HTTP METHOD", httpServletRequest.getMethod());
        metaData.put("PATH INFO", httpServletRequest.getPathInfo());
        metaData.put("URL", AppUtil.getRequestUrl(httpServletRequest));
        metaData.put("ENV", Environment.INSTANCE.property(Environment.ENVIRONMENT));
        metaData.put("REMOTE ADDRESS", httpServletRequest.getRemoteAddr());
        metaData.put("REQUEST PARAMETERS", requestMap);
        getLogger().info("\n" + "MetaData: " + conversionUtil.printToJson(metaData) + "\n");
	}
	
	private void printRequestData(String content){
		getLogger().info("\n" + "RequestData: " + conversionUtil.prettyPrintJson(content) + "\n");
	}
	
	private void printResponseData(String content){
		getLogger().info("\n" + "ResponseData: " + conversionUtil.prettyPrintJson(content) + "\n");
	}
	
	public Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String)requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}
	
	@Override
	public void destroy() {
		
	}
	
	private Logger getLogger(){
		if(!"boot".equalsIgnoreCase(Environment.INSTANCE.property(Environment.ENVIRONMENT)))
			return DUMP_LOG;
		else
			return LOG;
	}
}