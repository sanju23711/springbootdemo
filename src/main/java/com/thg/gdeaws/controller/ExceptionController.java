package com.thg.gdeaws.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thg.gdeaws.exchange.Response;
import com.thg.gdeaws.service.ExceptionService;
import com.thg.gdeaws.util.AppUtil;

@RestController
@ControllerAdvice
@RequestMapping("/error")
public class ExceptionController implements ErrorController {
	
	static final Logger DEBUG_LOG = Logger.getLogger("debugLogger");
	static final Logger LOG = Logger.getLogger(ExceptionController.class);

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@Autowired
	private ExceptionService exceptionService;

	@RequestMapping
	@ExceptionHandler({Exception.class})
	public Response<Object> error(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Exception ex) {
		Response<Object> response = new Response<Object>(false, "Processing request is failed",
				AppUtil.getRequestUrl(httpServletRequest),
				null,
				exceptionService.parse(ex));
		DEBUG_LOG.error(ex, ex);
		LOG.error(ex, ex);
		return response;
	}
}