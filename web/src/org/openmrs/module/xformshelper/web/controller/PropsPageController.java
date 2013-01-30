package org.openmrs.module.xformshelper.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for properties page jsp
 * 
 * @author Samuel Mbugua
 */
@Controller
public class PropsPageController {
	
	@RequestMapping(value="/module/xformshelper/propertiesPage", method=RequestMethod.GET)
	public Map<String, Object> populateForm() {
		XformsHelperService xhs = (XformsHelperService)Context.getService(XformsHelperService.class);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("systemVars", xhs.getSystemVariables());
		return map;
	}
}
