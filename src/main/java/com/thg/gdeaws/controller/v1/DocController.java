package com.thg.gdeaws.controller.v1;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.thg.gdeaws.exchange.WebService;
import com.thg.gdeaws.service.ApiService;
import com.thg.gdeaws.setup.environment.Environment;

@Controller
public class DocController {
	static final Logger LOG = Logger.getLogger(DocController.class);
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView hello() {
    	WebService webService = new WebService();
        webService.setServiceName("Hibbert Data Entry Auth Service");
        webService.setOwner("The Hibbert Group");
        webService.setContact(
        		"IT Department, "
        		+ "400 Pennington Ave, Trenton 08619, "
        		+ "mkandasamy@hibbertgroup.com, "
        		+ "+1 609 222 6098");
        webService.setUrl(Environment.INSTANCE.property(Environment.SITE_URL));
        webService.setVersion("v1");
        webService.setEndPoints(apiService.getEndpoints());
    	
        ModelAndView mav = new ModelAndView();
        mav.setViewName("documentation");
        mav.addObject("webService", webService);
        return mav;
    }
}
