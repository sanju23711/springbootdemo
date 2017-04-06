package com.thg.gdeaws.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thg.gdeaws.exception.ObjectValidationException;
import com.thg.gdeaws.exchange.EndPoint;
import com.thg.gdeaws.exchange.LoginOutput;
import com.thg.gdeaws.exchange.Request;
import com.thg.gdeaws.exchange.Response;
import com.thg.gdeaws.service.ApiService;
import com.thg.gdeaws.service.AuthService;
import com.thg.gdeaws.util.AppUtil;

@Controller
public class ValidationController {
	
	static final Logger LOG = Logger.getLogger(ValidationController.class);
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(
			value = "/v1/auth/validate/api", 
			produces = { "application/xml", "application/json" }, 
			method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<EndPoint> api(
    		HttpServletRequest httpServletRequest) throws ObjectValidationException {
		
		return apiService.getLoginValidationEndpoints();
	}
	
	@RequestMapping(
			value = "/v1/auth/validate/get", 
			produces = { "application/xml", "application/json" }, 
			method = RequestMethod.GET)
    @ResponseBody public Response<LoginOutput> validate(
    		HttpServletRequest httpServletRequest,
    		@RequestParam(required=false) String badge, 
    		@RequestParam(required=false) String account, 
    		@RequestParam(required=false) String username, 
    		@RequestParam(required=false) String password) throws ObjectValidationException {
		
		Request<Object> loginRequest = new Request<Object>(); 
		loginRequest.setBadge(badge);
		loginRequest.setAccount(account);
		loginRequest.setUsername(username);
		loginRequest.setPassword(password);
		return validate(httpServletRequest, loginRequest);
	}
	
	@RequestMapping(
			value = "/v1/auth/validate/post", 
			produces = { "application/xml", "application/json" }, 
			method = RequestMethod.POST)
    @ResponseBody public Response<LoginOutput> validate(
    		HttpServletRequest httpServletRequest,
    		@RequestBody Request<Object> loginRequest) throws ObjectValidationException {
		
		LoginOutput loginOutput = authService.validate(loginRequest);
		Response<LoginOutput> response = new Response<LoginOutput>();
		response.setSuccess(true);
		response.setDescription("Login validation is successful");
		response.setUrl(AppUtil.getRequestUrl(httpServletRequest));
		response.setOutput(loginOutput);
		return response;
	}
}
