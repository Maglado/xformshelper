package org.openmrs.module.xformshelper.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Form;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.odkconnector.api.service.ConnectorService;
import org.openmrs.module.xforms.Xform;
import org.openmrs.module.xforms.XformsService;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.openmrs.module.xformshelper.XformsHelperXform;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Controller for Downloadable xforms jsp page
 * 
 * @author Samuel Mbugua
 */
@Controller
public class DownloadableXformsController {
	private static final Log log = LogFactory.getLog(DownloadableXformsController.class);
	private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder docBuilder;
	private XformsHelperService xfhs;
	
	@RequestMapping(value="/module/xformshelper/downloadableXforms", method=RequestMethod.GET)
	public ModelMap populateForm(ModelMap map) {
		getXformsHelperService();
		XformsService xfs = Context.getService(XformsService.class);
		FormService fs = Context.getFormService();
		map.addAttribute("downloadableXforms", xfhs.getAllDownloadableXforms());
		
		//Add all Forms
		List<Xform> xforms = xfs.getXforms();
		List<Form> forms = new ArrayList<Form>();
		for (Xform xform : xforms) {
			try{
				Form form = fs.getForm(xform.getFormId());
				if (form != null)
					forms.add(form);
			}catch (Exception e) {
			}
		}
		map.addAttribute("forms", forms);
		
		//add programs to map
		ConnectorService cs = Context.getService(ConnectorService.class);
		map.addAttribute("programs", cs.getConceptConfigurations());
		return map;
	}
	
	@RequestMapping(value="/module/xformshelper/downloadableXforms", method=RequestMethod.POST)
	public String saveObject(HttpSession httpSession,HttpServletRequest request,
								@RequestParam(value ="xformsHelperXformId", required=false) List<Integer> listXformIds,
								@RequestParam(value="form", required=false) Integer formIdentifier,
								@RequestParam(value="program", required=false) Integer program){
		MessageSourceService mss = Context.getMessageSourceService();
		XformsService xfs = Context.getService(XformsService.class);
		FormService fs = Context.getFormService();
		
		String action = request.getParameter("action");
		String upload = mss.getMessage("xformshelper.downloadableXforms.upload");
		String delete = mss.getMessage("xformshelper.downloadableXforms.delete");
		String loadXform = mss.getMessage("xformshelper.downloadableXforms.loadXform");
		
		if (action.equalsIgnoreCase(upload)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("resourceFile");
			getXformsHelperService();
			try {
				if (multipartFile != null && !multipartFile.isEmpty()) {
					
					//check that ids exist
					Integer formId = getFormId(multipartFile.getInputStream());
					if (formId != -1){
						if (Context.getFormService().getForm(formId) != null){
							XformsHelperXform xform;
							//make convenience checks
							xform=xfhs.getDownloadableXformByFormId(formId);
							if (xform == null) {
								xform=xfhs.getDownloadableXformByName(multipartFile.getOriginalFilename());
								if (xform != null){
									httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "A form with same name (" + multipartFile.getOriginalFilename() 
										+ ") exists. Delete it and try again.");
									return "redirect:downloadableXforms.list";
								}
							}else{
								httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "A form with same form-id (" + formId 
										+ ") exists. Delete it and try again.");
								return "redirect:downloadableXforms.list";
							}
							xform = new XformsHelperXform();
							xform.setXformId(formId);
							xform.setXformName(multipartFile.getOriginalFilename());
							xform.setXformMeta(getFormMeta());
							xform.setXformXml(IOUtils.toString(multipartFile.getInputStream()));
							xform.setProgram(program);
							xfhs.saveDownloadableXform(xform);
							httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Successfully uploaded " + multipartFile.getOriginalFilename());
						}else
							httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "This form does not exist in the openmrs database");
					} else
						httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Invalid file format");
				}else
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "No file selected for upload or it is corrupted");
			} catch (IllegalStateException e) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Error uploading file");
				e.printStackTrace();
			} catch (IOException e) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Error uploading file");
				e.printStackTrace();
			} 
		}
		
		if (action.equalsIgnoreCase(delete)) {
			if (listXformIds.size() > 0) {
				getXformsHelperService();
				for (Integer xformsHelperXformId : listXformIds) {
					XformsHelperXform xformsHelperXform = xfhs.getDownloadableXform(xformsHelperXformId);
					if (xformsHelperXform != null)
						xfhs.deleteDownloadableXform(xformsHelperXform);
				}
			}
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Xform(s) successfully deleted");
		}
		
		if (action.equalsIgnoreCase(loadXform)) {
			Form form = fs.getForm(formIdentifier);
			Xform xforma = xfs.getXform(form);
			if (xforma != null){
				XformsHelperXform xformHelper=xfhs.getDownloadableXformByFormId(xforma.getFormId());
				if (xformHelper != null){
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "A form with same form-id (" + formIdentifier 
							+ ") exists. Delete it and try again.");
				} else {
					XformsHelperXform xform = new XformsHelperXform();
					xform.setXformId(xforma.getFormId());
					xform.setXformName(form.getName()+ ".xml");
					xform.setXformMeta(getFormMeta());
					xform.setXformXml(xforma.getXformXml());
					xform.setProgram(program);
					xfhs.saveDownloadableXform(xform);
					httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Successfully loaded Xform");
				}
			}
		}
		
		return "redirect:downloadableXforms.list";
	}
	
	private String getFormMeta() {
		Date d = new Date();
		return new SimpleDateFormat("EEE, MMM dd, yyyy 'at' HH:mm").format(d);
	}
	
	private void getXformsHelperService() {
		if (xfhs == null)
			xfhs = Context.getService(XformsHelperService.class);
	}
	
	private Integer getFormId(InputStream is){
		Document doc;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(is);
			NodeList nodeList = doc.getElementsByTagName("form");
			if (nodeList.getLength()>0){
				Node rootNode= nodeList.item(0);
				NamedNodeMap nodeMap=rootNode.getAttributes();
				String formId = nodeMap.getNamedItem("id").getNodeValue();
				return Integer.parseInt(formId);
			}
		} catch (SAXException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage());
		}
		return -1;
	}
}	