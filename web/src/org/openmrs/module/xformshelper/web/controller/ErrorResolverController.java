package org.openmrs.module.xformshelper.web.controller;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.xformshelper.XformsError;
import org.openmrs.module.xformshelper.XformsHelperConstants;
import org.openmrs.module.xformshelper.XformsHelperErrorModel;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.openmrs.module.xformshelper.XformsQueue;
import org.openmrs.module.xformshelper.util.LogArchiver;
import org.openmrs.module.xformshelper.util.XformEditor;
import org.openmrs.module.xformshelper.util.XformsHelperUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

/**
 * Controller for xforms errors resolution jsp pages
 * 
 * @author Samuel Mbugua
 */
@Controller
public class ErrorResolverController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private XformsHelperService mfs;
	
	/**
	 * Controller for Error list jsp page
	 */
	@ModelAttribute("xformsErrors")
	@RequestMapping(value="/module/xformshelper/resolveErrors", method=RequestMethod.GET)
	public List<XformsError> populateForm() {
		if (Context.isAuthenticated()) {
			getXformsHelperService();
			List<XformsError> lstErrors = mfs.getAllXformsErrors();
			List<XformsError> tmpLstErrors = new ArrayList<XformsError>();
			for (XformsError xformsError : lstErrors) {
				if (createFormData(xformsError.getFormName())!=null)
					tmpLstErrors.add(xformsError);
			}
			return tmpLstErrors;
		}
		return null;
	}
	
	@ModelAttribute("warnings")
	@RequestMapping(value="/module/xformshelper/processingWarning", method=RequestMethod.GET)
	public List<XformsError> populateWarnings() {
		if (Context.isAuthenticated()) {
			getXformsHelperService();
			List<XformsError> lstWarnings = mfs.getAllXformsErrors();
			List<XformsError> tmpLstWarnings = new ArrayList<XformsError>();
			for (XformsError xformsError : lstWarnings) {
				if (createFormData(xformsError.getFormName())==null)
					tmpLstWarnings.add(xformsError);
			}
			return tmpLstWarnings;
		}
		return null;
	}
	
	@RequestMapping(value="/module/xformshelper/processingWarning", method=RequestMethod.POST)
	public String archiveErrorLogs(HttpSession httpSession) {
		getXformsHelperService();
		MessageSourceService msa = Context.getMessageSourceService();
		//get all erred logs
		StringBuffer outPutLine = new StringBuffer();
		List<XformsError> lstWarnings = mfs.getAllXformsErrors();
		List<XformsError> tmpLstWarnings = new ArrayList<XformsError>();
		for (XformsError xformsError : lstWarnings) {
			if (createFormData(xformsError.getFormName())==null)
				tmpLstWarnings.add(xformsError);
		}
		for (XformsError XformsError : tmpLstWarnings) {
			outPutLine.append(getOutputLine(XformsError));
		}
		
		if (outPutLine.length()> 0) {
			if (LogArchiver.createArchiveLog(outPutLine.toString(), "Problem_log")){
				for (XformsError xformsError : tmpLstWarnings) {
					mfs.deleteError(xformsError);
				}
				httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, 
						msa.getMessage("xformshelper.processingWarning.clear.success"));
			}else
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, 
						msa.getMessage("xformshelper.processingWarning.clear.error"));
		}
		return "/module/xformshelper/processingWarning";
	}
	
	/**
	 * Controller for commentOnError jsp Page
	 */
	@ModelAttribute("errorFormComment")
	@RequestMapping(value="/module/xformshelper/resolveErrorComment", method=RequestMethod.GET)
	public XformsHelperErrorModel populateCommentForm(@RequestParam Integer errorId) {
		return getErrorObject(errorId);
	}
	
	/**
	 * Controller for commentOnError post jsp Page
	 */
	@RequestMapping(value="/module/xformshelper/resolveErrorComment", method=RequestMethod.POST)
	public String saveComment(HttpSession httpSession, @RequestParam Integer errorId, @RequestParam String comment) {
		if (comment.trim().length() > 0) {
			getXformsHelperService();
			XformsError error=mfs.getErrorById(errorId);
			error.setComment(comment);
			error.setCommentedBy(Context.getAuthenticatedUser());
			error.setDateCommented(new Date());
			mfs.saveErrorInDatabase(error);
		}else
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Invalid Comment" );
		return "redirect:resolveErrors.list";		
	}
	
	/**
	 * Controller for formDisplay pop-up jsp Page
	 */
	@RequestMapping(value="/module/xformshelper/formDisplay", method=RequestMethod.GET)
	public String populateDisplayForm(ModelMap model, HttpServletRequest request) {
		String strErrorId = request.getParameter("errorId");
		try {
			getXformsHelperService();
			Integer errorId = Integer.parseInt(strErrorId);
			XformsError error=mfs.getErrorById(errorId);
			if (error != null)
				model.addAttribute("errorObject", getErrorObject(errorId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/module/xformshelper/formDisplay";
	}

	/**
	 * Controller for resolveError jsp Page
	 */
	@ModelAttribute("errorFormResolve")
	@RequestMapping(value="/module/xformshelper/resolveError", method=RequestMethod.GET)
	public XformsHelperErrorModel populateErrorForm(@RequestParam Integer errorId) {
		return getErrorObject(errorId);	
	}


	/**
	 * Controller for resolveError post jsp Page
	 */
	@RequestMapping(value="/module/xformshelper/resolveError", method=RequestMethod.POST)
	public String resolveError(HttpSession httpSession, @RequestParam String givenName,
								@RequestParam Integer errorId, @RequestParam String errorItemAction,
								@RequestParam String birthDate, @RequestParam String patientIdentifier,
								@RequestParam String providerId, @RequestParam String familyName,
								@RequestParam String middleName, @RequestParam String gender){
		String filePath;
		
		// user must be authenticated (avoids authentication errors)
		if (Context.isAuthenticated()) {
			if (!Context.getAuthenticatedUser().hasPrivilege(
					XformsHelperConstants.PRIV_RESOLVE_XFORM_ERROR)) {
				httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "xformshelper.action.noRights");
				return "redirect:resolveErrors.list";
			}
				
			getXformsHelperService();
			
			// fetch the XformsError item from the database
			XformsError errorItem = mfs.getErrorById(errorId);
			filePath= XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath() + errorItem.getFormName();
			
			// assign birth-date
			if ("assignBirthdate".equals(errorItemAction)) {
				if (birthDate!=null && birthDate.trim()!="") {
					if (XformEditor.editNode(filePath, 
							XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_BIRTHDATE, birthDate)) {
						// put form in queue for normal processing
						saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
						// delete the xform error queue item
						mfs.deleteError(errorItem);
					} 
				}else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Birthdate was not assigned, Null object entered");
					return "redirect:resolveErrors.list";
				}
			}
			
			// assign gender
			if ("assignGender".equals(errorItemAction)) {
				if (gender!=null && gender.trim()!="") {
					if (XformEditor.editNode(filePath, 
							XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_SEX, gender)) {
						// put form in queue for normal processing
						saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
						// delete the xform error queue item
						mfs.deleteError(errorItem);
					} else {
						httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Gender was not assigned, Node does not exist in this form");
						return "redirect:resolveErrors.list";
					}
				}else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Gender was not assigned, You did not select gender to assign");
					return "redirect:resolveErrors.list";
				}
			}
			
			// assign person names
			if ("assignNames".equals(errorItemAction)) {
				if (givenName != null && givenName.trim() != "" 
					&& familyName != null && familyName.trim() != "") {
					boolean editted = false;
					editted = XformEditor.editNode(filePath, 
							XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_GIVENNAME, givenName);
					editted = XformEditor.editNode(filePath, 
							XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_FAMILYNAME, familyName);
					editted = XformEditor.editNode(filePath, 
							XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_MIDDLENAME, middleName);
					if (editted){
						// put form in queue for normal processing
						saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
						// delete the xform error queue item
						mfs.deleteError(errorItem);
					}else {
						httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Error editting person names");
						return null;
					}
				} else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Family and Given Names are required!");
					return null;
				}
			}
			
			// assign a new identifier
			if ("newIdentifier".equals(errorItemAction)) {
				if (patientIdentifier != null && patientIdentifier.trim() != "") {
					if (reverseNodes(filePath, patientIdentifier)) {
						
						// put form in queue for normal processing
						saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
						
						// delete the xform error queue item
						mfs.deleteError(errorItem);
					}
				}
				else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "xformshelper.resolveErrors.action.newIdentifier.error");
					return "redirect:resolveErrors.list";
				}
			}
			
			// link a provider
			if ("linkProvider".equals(errorItemAction)) {
				if (providerId != null && providerId.trim() != "") {
					providerId = Context.getUserService().getUser(Integer.parseInt(providerId)).getSystemId();
					if (XformEditor.editNode(filePath, 
							XformsHelperConstants.ENCOUNTER_NODE + "/" + XformsHelperConstants.ENCOUNTER_PROVIDER, providerId)) {
						// put form in queue for normal processing
						saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
						// delete the xform error queue item
						mfs.deleteError(errorItem);
					}
				}
				else {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "(Null) Invalid provider ID");
					return "redirect:resolveErrors.list";
				}
			}
			
			if ("editXform".equals(errorItemAction)) {
				return "redirect:editXform.form?errorId="+errorId;
			}
			else if ("createPatient".equals(errorItemAction)) {
				// put form in queue for normal processing
				saveForm(filePath, XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath() + errorItem.getFormName());
				
				// delete the xform error queue item
				mfs.deleteError(errorItem);				
			}
			else if ("deleteError".equals(errorItemAction)) {
				// delete the xform error queue item
				mfs.deleteError(errorItem);
				
				//and delete from the file system
				XformsHelperUtil.deleteFile(filePath);
				
			}
			else if ("deleteComment".equals(errorItemAction)) {
				//set comment to null and save
				errorItem.setComment(null);
				mfs.saveErrorInDatabase(errorItem);
			}
			
			else if ("noChange".equals(errorItemAction)) {
				// do nothing here
			}
		}
		
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "xformshelper.resolveErrors.action.success"); 
		return "redirect:resolveErrors.list";		
	}
	
	/**
	 * Controller for editXform jsp Page
	 *
	@ModelAttribute("xformEditModels")
	@RequestMapping(value="/module/xformshelper/editXform", method=RequestMethod.GET)
	public List<XformEditModel> showEditXform(HttpServletRequest request) {
		String strErrorId = request.getParameter("errorId");
		try {
			getXformsHelperService();
			Integer errorId = Integer.parseInt(strErrorId);
			XformsError error=mfs.getErrorById(errorId);
			if (error != null) {
				String filePath= XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath() + error.getFormName();
				return XFormEditor.createMapFromFile(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Controller for editXform jsp Page
	 */
	@ModelAttribute("xformEditModels")
	@RequestMapping(value="/module/xformshelper/editXform", method=RequestMethod.GET)
	public String showEditXform(ModelMap model, HttpServletRequest request) {
		String strErrorId = request.getParameter("errorId");
		try {
			getXformsHelperService();
			Integer errorId = Integer.parseInt(strErrorId);
			XformsError error=mfs.getErrorById(errorId);
			if (error != null)
				model.addAttribute("errorObject", getErrorObject(errorId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/module/xformshelper/editXform";
	}
	
	/**
	 * Controller for editXform jsp Page
	 */
	@RequestMapping(value="/module/xformshelper/editXform", method=RequestMethod.POST)
	public String saveEditedXform(HttpSession httpSession, @RequestParam String xmlString,
			 @RequestParam Integer errorId, @RequestParam String action) {
		if (action != null) {
			MessageSourceService mss = Context.getMessageSourceService();
			String save = mss.getMessage("xformshelper.resolveErrors.editXform.save");
			if (action.equals(save)) {
				try {
					getXformsHelperService();
					XformsError error=mfs.getErrorById(errorId);
					if (error != null){
						String filePath = XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath() + error.getFormName();
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = factory.newDocumentBuilder();
						Document doc = docBuilder.parse(IOUtils.toInputStream(xmlString));
						XformEditor.saveXMLDocument(doc, filePath);
						httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "xformshelper.resolveErrors.editXform.success");
					}
				} catch (Exception e) {
					httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "xformshelper.resolveErrors.editXform.error");
					e.printStackTrace();
				}
			}
		}
		return "redirect:resolveError.form?errorId=" + errorId;
	}
	
	private String formatXML(String xmlString) {
		try {
			Source xmlInput = new StreamSource(new StringReader(xmlString));
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}  
		return xmlString;
	}
	
	/**
	 * Given an id, this method  creates an error model
	 * @param errorId
	 * @return List of errors
	 */
	private XformsHelperErrorModel  getErrorObject(Integer errorId) {
		getXformsHelperService();
		XformsError error= mfs.getErrorById(errorId);
		if (error !=null) {
			String formName = error.getFormName();
			String filePath = getAbsoluteFilePath(formName);
			error.setFormName(formatXML((createFormData(error.getFormName()))));
			XformsHelperErrorModel errorForm = new XformsHelperErrorModel(error);
			errorForm.setFormPath(filePath);
			return errorForm;
		}
		return null;
	}
	
	/**
	 * Converts an xml file specified by <b>formPath</b> to a string
	 * @param formPath
	 * @return String representation of the file
	 */
	private String createFormData (String formName) {
		getXformsHelperService();
		XformsQueue queue= mfs.getXformsHelperQueue(XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath()
								+ formName);
		return queue.getFormData();
	}
	
	/**
	 * Takes in an Xform Queue and returns an absolute Path
	 * @param formPath
	 * @return String absolute path of the file
	 */
	private String getAbsoluteFilePath (String formName) {
		getXformsHelperService();
		XformsQueue queue= mfs.getXformsHelperQueue(XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath()
								+ formName);
		return queue.getFileSystemUrl();
	}

	/**
	 * Stores a form in a specified folder
	 */
	private void saveForm(String oldFormPath, String newFormPath){
		try{
			if(oldFormPath != null){
				File file=new File(oldFormPath);
				
				//move the file to specified new directory
				file.renameTo(new File(newFormPath));
			}
		}
		catch(Exception e){
			log.error(e.getMessage(),e);
		}

	}
	
	/**
	 * Reverses patient Identifier nodes for a form with more than one
	 * @param filePath
	 * @param patientIdentifier
	 * @return
	 */
	private boolean reverseNodes(String filePath, String patientIdentifier) {
		try {
			XformEditor.editNode(filePath, XformsHelperConstants.PATIENT_NODE + "/" + XformsHelperConstants.PATIENT_IDENTIFIER, patientIdentifier);
		}
		catch (Throwable t) {
			log.error("Error reversing nodes", t);
			return false;
		}
		return true;
	}
	
	private String getOutputLine(XformsError xformsError) {
		return xformsError.getId() + ", " + xformsError.getError() + ", " + xformsError.getErrorDetails() 
		+ ", " + xformsError.getFormName() + ", " + xformsError.getDateCreated() + "\n";
	}
	
	private void getXformsHelperService(){
		if (mfs == null)
			mfs=(XformsHelperService)Context.getService(XformsHelperService.class);
	}
}	