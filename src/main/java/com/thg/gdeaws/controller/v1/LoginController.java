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
public class LoginController {
	
	static final Logger LOG = Logger.getLogger(LoginController.class);
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(
			value = "/v1/auth/login/api", 
			produces = { "application/xml", "application/json" }, 
			method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<EndPoint> api(
    		HttpServletRequest httpServletRequest) throws ObjectValidationException {
		
		return apiService.getLoginEndpoints();
	}
	
	@RequestMapping(
			value = "/v1/auth/login/get", 
			produces = { "application/xml", "application/json" }, 
			method = RequestMethod.GET)
    @ResponseBody public Response<LoginOutput> login(
    		HttpServletRequest httpServletRequest,
    		@RequestParam String account, 
    		@RequestParam String username, 
    		@RequestParam String password) throws ObjectValidationException {
		
		Request<Object> loginRequest = new Request<Object>(); 
		loginRequest.setAccount(account);
		loginRequest.setUsername(username);
		loginRequest.setPassword(password);
		return login(httpServletRequest, loginRequest);
	}
	
	@RequestMapping(
			value = "/v1/auth/login/post", 
			produces = { "application/xml", "application/json" }, 
			method = RequestMethod.POST)
    @ResponseBody public Response<LoginOutput>  login(
    		HttpServletRequest httpServletRequest,
    		@RequestBody Request<Object> loginRequest) throws ObjectValidationException {
		
		LoginOutput loginOutput = authService.login(loginRequest);
		Response<LoginOutput> response = new Response<LoginOutput>();
		response.setSuccess(true);
		response.setDescription("Login is successful");
		response.setUrl(AppUtil.getRequestUrl(httpServletRequest));
		response.setOutput(loginOutput);
    	
		return response;
	}
}
