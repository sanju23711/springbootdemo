package com.thg.gdeaws.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import com.thg.gdeaws.util.AppUtil;
import com.thg.gdeaws.util.ErrorListUtil;

@Controller
public class BaseController {

    @RequestMapping("/**")
    @ResponseBody String home(HttpServletRequest request) throws ResourceAccessException {
		throw new ResourceAccessException("No Resource available. " + AppUtil.getRequestUrl(request));
	}
    
    @RequestMapping(value = "/errors", method = RequestMethod.GET)
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors");
        mav.addObject("errorList", ErrorListUtil.getErrorList());
        return mav;
    }
}