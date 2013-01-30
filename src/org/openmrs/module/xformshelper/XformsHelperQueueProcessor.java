package org.openmrs.module.xformshelper;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.xformshelper.util.XformEditor;
import org.openmrs.module.xformshelper.util.XformsHelperFileUploader;
import org.openmrs.module.xformshelper.util.XformsHelperUtil;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Processes Patient Xforms in the Queue directory
 * Submits all the patient forms to the xforms module for processing
 * 
 * @author Samuel Mbugua
 *
 */
@Transactional
public class XformsHelperQueueProcessor {

	private static final Log log = LogFactory.getLog(XformsHelperQueueProcessor.class);
	private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder docBuilder;
	private XPathFactory xPathFactory;
	private XformsHelperService xfhs;
	// allow only one running instance
	private static Boolean isRunning = false; 

	public XformsHelperQueueProcessor(){
		try{
			docBuilder = docBuilderFactory.newDocumentBuilder();
			this.getXformsHelperService();
		}
		catch(Exception e){
			log.error("Problem occurred while creating document builder", e);
		}
	}
	/**
	 * Process an existing entry in the queue directory
	 * @param filePath 
	 */
	private void processQueueForms(String filePath, XformsQueue queue) throws APIException {
		log.debug("Processing patient forms and sending them to the xform module");
		try {
			String formData = queue.getFormData();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			XPathFactory xpf = getXPathFactory();
			XPath xp = xpf.newXPath();
			Document doc = docBuilder.parse(IOUtils.toInputStream(formData));
			Node curNode=(Node)  xp.evaluate(XformsHelperConstants.PATIENT_NODE, doc, XPathConstants.NODE);
			String patientId = xp.evaluate(XformsHelperConstants.PATIENT_ID, curNode);
			String patientIdentifier = xp.evaluate(XformsHelperConstants.PATIENT_IDENTIFIER, curNode); 
			String birthDate = xp.evaluate(XformsHelperConstants.PATIENT_BIRTHDATE, curNode);
			String familyName = xp.evaluate(XformsHelperConstants.PATIENT_FAMILYNAME, curNode);
			String givenName = xp.evaluate(XformsHelperConstants.PATIENT_GIVENNAME, curNode);
			String middleName = xp.evaluate(XformsHelperConstants.PATIENT_MIDDLENAME, curNode);
			
			if (patientId != null) patientId=patientId.trim(); // remove white spaces in patient_id
			
			//If a form has a non-zero patient id its an existing patient forget everything, 
			//otherwise do demographics check
			if (patientId == null || patientId.equalsIgnoreCase("0")) {
				//Ensure there is a patient identifier in the form and 
				// if without names just delete the form
				String tmpIdentifier = XformsHelperUtil.getPatientIdentifier(doc);
				if (tmpIdentifier == null || tmpIdentifier.trim() == "") {
					if ((familyName == null || familyName.trim() == "") &&
							(givenName == null || givenName == "")) {
						XformsHelperUtil.deleteFile(filePath);
						log.info("Deleted an empty individual file");
					} else {
						// form has no patient identifier but has names : move to error
						saveFormInError(filePath);
						xfhs.saveErrorInDatabase(XformsHelperUtil.
								createError(getFormName(filePath), "Error processing patient", 
										"Patient has no identifier, or the identifier provided is invalid"));
					}
					return;
				}
				
				//Ensure Family name and Given names are not blanks
				if (familyName == null || familyName.trim() == "" || givenName == null || givenName == "") {
					saveFormInError(filePath);
					xfhs.saveErrorInDatabase(XformsHelperUtil.
							createError(getFormName(filePath), "Error processing patient", 
									"Patient has no valid names specified, Family Name and Given Name are required"));
					return;
				}
				
				// ensure patient has birth date
				if (birthDate == null || birthDate.trim().length() == 0 ) {
					//patient has no valid birth-date
					saveFormInError(filePath);
					xfhs.saveErrorInDatabase(XformsHelperUtil.
							createError(getFormName(filePath), "Error processing patient", "Patient has no valid Birthdate"));
					return;
				}
			}
			
			// Ensure there is a valid provider id or name and return provider_id in the form
			curNode=(Node)  xp.evaluate(XformsHelperConstants.ENCOUNTER_NODE, doc, XPathConstants.NODE);
			Integer providerId=XformsHelperUtil.getProviderId(xp.evaluate(XformsHelperConstants.ENCOUNTER_PROVIDER, curNode));
			if ((providerId) == null) {
				// form has no valid provider : move to error
				saveFormInError(filePath);
				xfhs.saveErrorInDatabase(XformsHelperUtil.createError(getFormName(filePath), "Error processing patient form", 
								"Provider for this encounter is not provided, or the provider identifier provided is invalid"));
				return;
			}else
				XformEditor.editNode(filePath, 
						XformsHelperConstants.ENCOUNTER_NODE + "/" + XformsHelperConstants.ENCOUNTER_PROVIDER, providerId.toString());
			
			//Ensure if not new it is same person
			if (!XformsHelperUtil.isNewPatient(patientIdentifier)){
				Patient pat = XformsHelperUtil.getPatient(patientIdentifier);
				PersonName personName = new PersonName(givenName, middleName, familyName);
				if (!pat.getPersonName().equalsContent(personName)) {
					saveFormInError(filePath);
					xfhs.saveErrorInDatabase(XformsHelperUtil.
							createError(getFormName(filePath), "Error processing patient", 
									"A different person (By Name) exists with the same identifier " + "(" + patientIdentifier + ")."));
					return;
				}
			}
			
			String enterer = "";
			// here providerId is personId not userId so get user id 
			List<User> users = Context.getUserService().getUsersByPerson(Context.getPersonService().getPerson(providerId), false);
			
			if (users != null && users.size() == 1 ) {
				User user = users.get(0);
				if (user != null)
					enterer = user.getUserId() + "^" + user.getGivenName() + " " + user.getFamilyName();
			}				
					
			//Finally send to xforms for processing
			XformsHelperFileUploader.submitXFormFile(filePath, enterer);
			saveFormInArchive(filePath);
		}
		catch (Exception e) {
			String error = e.getMessage();
			log.error("Error while sending form to xform module", e);
			//put file in error queue
			saveFormInError(filePath);
			xfhs.saveErrorInDatabase(XformsHelperUtil.createError(getFormName(filePath), 
					"Error sending form to xform module", error != null?error:"Could not get details of this error. Technical investigation may help"));
		}
	}

	/**
	 * Processes each queue entry. If there are no pending
	 * items in the queue, this method simply returns quietly.
	 */
	public void processXformsHelperQueue() {
		synchronized (isRunning) {
			if (isRunning) {
				log.warn("XformsHelperQueue processor aborting (another processor already running)");
				return;
			}
			isRunning = true;
		}

		try {			
			File queueDir = XformsHelperUtil.getXformsHelperQueueDir();
			for (File file : queueDir.listFiles()) {
				XformsQueue queue = xfhs.getXformsHelperQueue(file.getAbsolutePath());
				processQueueForms(file.getAbsolutePath(), queue);
			}
		}
		catch(Exception e){
			log.error("Problem occured while processing queue", e);
		}
		finally {
			isRunning = false;
		}
	}

	/**
	 * Stores an erred form in the error directory
	 * @param formPath 
	 */
	private void saveFormInError(String formPath){
		String errorFilePath= XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath() + getFormName(formPath);
		saveForm(formPath, errorFilePath);
	}
	
	/**
	 * Stores a new patient file to pending link directory
	 * @param formPath 
	 */
	private void saveFormInArchive(String formPath){
		String archiveFilePath= XformsHelperUtil.getXformsHelperArchiveDir(new Date()).getAbsolutePath() + getFormName(formPath);
		saveForm(formPath, archiveFilePath);
	}

	/**
	 * Stores a form in a specified folder after processing.
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
	 * Extracts form name from an absolute file path
	 * @param formPath
	 * @return
	 */
	private String getFormName(String formPath) {
		return formPath.substring(formPath.lastIndexOf(File.separatorChar)); 
	}
	
	/**
	 * @return XPathFactory to be used for obtaining data from the parsed XML
	 */
	private XPathFactory getXPathFactory() {
		if (xPathFactory == null)
			xPathFactory = XPathFactory.newInstance();
		return xPathFactory;
	}
	
	/**
	 * @return XformsHelperService to be used by the process
	 */
	private XformsHelperService getXformsHelperService() {
		if (xfhs == null) {
			try {
				xfhs= (XformsHelperService)Context.getService(XformsHelperService.class);
			}catch (APIException e) {
				log.debug("XformsHelperService not found");
				return null;
			}
		}
		return xfhs;
	}
	
}