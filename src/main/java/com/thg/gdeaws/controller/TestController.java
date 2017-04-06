package com.thg.gdeaws.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thg.gdeaws.exception.ObjectValidationException;
import com.thg.gdeaws.exchange.Response;
import com.thg.gdeaws.exchange.TestInput;
import com.thg.gdeaws.exchange.TestOutput;
import com.thg.gdeaws.setup.environment.Environment;
import com.thg.gdeaws.setup.environment.EnvironmentLoader;
import com.thg.gdeaws.util.AppUtil;
import com.thg.gdeaws.util.ValidationUtil;

@Controller
public class TestController {
	
	static final Logger LOG = Logger.getLogger(TestController.class);
	
	@Autowired
	private ValidationUtil validationUtil;

	@RequestMapping(value = "/test")
    @ResponseBody Response<TestOutput> test(HttpServletRequest request) throws ObjectValidationException {
		LOG.info("inside TestController");
		EnvironmentLoader environmentLoader = Environment.INSTANCE.bean(EnvironmentLoader.class, "environmentLoader");
		environmentLoader.load();
		Response<TestOutput> response = new Response<TestOutput>(
    			true,
    			"Request is successful",
    			AppUtil.getBaseUrl(request),
    			new TestOutput(){{setOne("one"); setTwo(2);}},
    			null
    			);
    	
		return response;
	}
	
	@RequestMapping(value = "/test2")
    @ResponseBody Response<TestOutput> test2(HttpServletRequest request,
    		@RequestBody TestInput testInput) throws ObjectValidationException {
		
		validationUtil.rejectIfInValid(testInput);
    	
    	Response<TestOutput> response = new Response<TestOutput>(
    			true,
    			"Request is succeefull",
    			AppUtil.getBaseUrl(request),
    			new TestOutput(){{setOne("one"); setTwo(2);}},
    			null
    			);
    	
		return response;
	}
	
}
