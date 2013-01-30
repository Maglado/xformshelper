package org.openmrs.module.xformshelper.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.xformshelper.XformsError;
import org.openmrs.module.xformshelper.XformsHelperConstants;
import org.openmrs.util.OpenmrsUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides utilities needed when processing xforms.
 * 
 * @author Samuel Mbugua
 */

public class XformsHelperUtil {
	private static Log log = LogFactory.getLog(XformsHelperUtil.class);

	public static File getXformsHelperArchiveDir(Date date) {
		AdministrationService as = Context.getAdministrationService();
    	String xformsHelperArchiveFileName = as.getGlobalProperty(XformsHelperConstants.GP_XFORMS_HELPER_ARCHIVE_DIR, XformsHelperConstants.GP_XFORMS_HELPER_ARCHIVE_DIR_DEFAULT);
    	
    	// replace %Y %M %D in the folderName with the date
		String folderName = replaceVariables(xformsHelperArchiveFileName, date);
		
		// get the file object for this potentially new file
		File xformsHelperArchiveDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper archive directory from global properties: " + xformsHelperArchiveDir.getAbsolutePath());
    	
		return xformsHelperArchiveDir;
	}
	
	public static File getXformsHelperSyncLogDir() {
    	
		String folderName = XformsHelperConstants.GP_XFORMS_HELPER_SYNC_LOG_DIR_DEFAULT;
		// get the file object for this potentially new file
		File xformsHelperSyncLogDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper sync log directory from global properties: " + xformsHelperSyncLogDir.getAbsolutePath());
    	
		return xformsHelperSyncLogDir;
	}
	
	public static File getXformsHelperErrorDir() {
		AdministrationService as = Context.getAdministrationService();
		String folderName = as.getGlobalProperty(XformsHelperConstants.GP_XFORMS_HELPER_ERROR_DIR,
													XformsHelperConstants.GP_XFORMS_HELPER_ERROR_DIR_DEFAULT);
		File xformsHelperErrorDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper error directory from global properties: " + xformsHelperErrorDir.getAbsolutePath());
		
		return xformsHelperErrorDir;
	}
	
	/**
	 * Directory where forms are placed pending to be split into individual persons
	 * @return directory
	 */
	public static File getXformsHelperPendingSplitDir() {
		String folderName = XformsHelperConstants.GP_XFORMS_HELPER_PENDING_SPLIT_DIR;
		File xformsHelperPendingSplitDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper pending split directory from global properties: " + xformsHelperPendingSplitDir.getAbsolutePath());
		
		return xformsHelperPendingSplitDir;
	}
	
	/**
	 * Directory where forms are placed pending post processing
	 * @return directory
	 */
	public static File getXformsHelperPostProcessDir() {
		String folderName = XformsHelperConstants.GP_XFORMS_HELPER_POST_PROCESS_DIR;
		File xformsHelperPostProcessDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper post process directory from global properties: " + xformsHelperPostProcessDir.getAbsolutePath());
		
		return xformsHelperPostProcessDir;
	}
	
	/**
	 * Directory where forms are dropped by remote devices
	 * @return directory
	 */
	public static File getXformsHelperDropDir() {
		AdministrationService as = Context.getAdministrationService();
		String folderName = as.getGlobalProperty(XformsHelperConstants.GP_XFORMS_HELPER_DROP_DIR,
													XformsHelperConstants.GP_XFORMS_HELPER_DROP_DIR_DEFAULT);
		File xformsHelperDropDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper drop queue directory from global properties: " + xformsHelperDropDir.getAbsolutePath());
		
		return xformsHelperDropDir;
	}
	
	public static File getXformsHelperQueueDir() {
		AdministrationService as = Context.getAdministrationService();
		String folderName = as.getGlobalProperty(XformsHelperConstants.GP_XFORMS_HELPER_QUEUE_DIR, 
													XformsHelperConstants.GP_XFORMS_HELPER_QUEUE_DIR_DEFAULT);
		File xformsHelperQueueDir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
		if (log.isDebugEnabled())
			log.debug("Loaded xforms helper queue directory from global properties: " + xformsHelperQueueDir.getAbsolutePath());
	
		return xformsHelperQueueDir;
	}
	
	/**
     * Replaces %Y in the string with the four digit year.
     * Replaces %M with the two digit month
     * Replaces %D with the two digit day
     * Replaces %w with week of the year
     * Replaces %W with week of the month
     * 
     * @param str String filename containing variables to replace with date strings 
     * @return String with variables replaced
     */
    public static String replaceVariables(String str, Date d) {
    	
    	Calendar calendar = Calendar.getInstance();
    	if (d != null)
    		calendar.setTime(d);
    	
    	int year = calendar.get(Calendar.YEAR);
    	str = str.replace("%Y", Integer.toString(year));
    	
    	int month = calendar.get(Calendar.MONTH) + 1;
    	String monthString = Integer.toString(month);
    	if (month < 10)
    		monthString = "0" + monthString;
    	str = str.replace("%M", monthString);
    	
    	int day = calendar.get(Calendar.DATE);
    	String dayString = Integer.toString(day);
    	if (day < 10)
    		dayString = "0" + dayString;
		str = str.replace("%D", dayString);
    	
    	int week = calendar.get(Calendar.WEEK_OF_YEAR);
    	String weekString = Integer.toString(week);
    	if (week < 10)
    		weekString = "0" + week;
		str = str.replace("%w", weekString);
    	
    	int weekmonth = calendar.get(Calendar.WEEK_OF_MONTH);
    	String weekmonthString = Integer.toString(weekmonth);
    	if (weekmonth < 10)
    		weekmonthString = "0" + weekmonthString;
		str = str.replace("%W", weekmonthString);
    	
    	return str;
    }
    
	public static boolean isNewPatient(String patientIdentifier) {
		List<Patient> patients=Context.getPatientService().getPatients(null,patientIdentifier,null, true);
		if (patients==null || patients.size()<1)
			return true;
		return false;
	}
	
	public static Patient getPatient(String patientIdentifier) {
		return Context.getPatientService().getPatients(null,patientIdentifier,null, false).get(0);
	}
	
	public static XformsError createError(String formName, String error, String errorDetails) {
		XformsError XformsHelperError=new XformsError();
		XformsHelperError.setFormName(formName);
		XformsHelperError.setError(error);
		XformsHelperError.setErrorDetails(errorDetails);
		return XformsHelperError;
	}
	
	/**
	 * Authenticate in-line users
	 * @param auth
	 * @return <b>true</b> if authentication was successful otherwise <b>false</b>
	 */
	public static boolean authenticate(String username, String password) {
		log.debug("Authenticating username: " + username + " with password: " + password);
		if(username != null & password != null)
			Context.authenticate(username, password);
		
		return Context.isAuthenticated();
	}
	
	/**
	 * Retrieves a patient identifier from a patient form
	 * @param doc
	 * @return patientIdentifier
	 */
	public static String getPatientIdentifier(Document doc){
		NodeList elemList = doc.getDocumentElement().getElementsByTagName("patient");
		if (!(elemList != null && elemList.getLength() > 0))
			return null;

		Element patientNode = (Element)elemList.item(0);

		NodeList children = patientNode.getChildNodes();
		int len = patientNode.getChildNodes().getLength();
		for(int index=0; index<len; index++){
			Node child = children.item(index);
			if(child.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if("patient_identifier".equalsIgnoreCase(((Element)child).getAttribute("openmrs_table")) &&
					"identifier".equalsIgnoreCase(((Element)child).getAttribute("openmrs_attribute")))
				return child.getTextContent();
		}

		return null;
	}
	
	/** Returns provider id given username
	 * @param userName
	 * @return
	 */
	public static Integer getProviderId(String userName) {
		User userProvider;
		Person personProvider;
		
		// assume its a normal user-name or systemId formatted with a dash
		userProvider = Context.getUserService().getUserByUsername(userName);
		if ( userProvider != null)
			return userProvider.getPerson().getPersonId();
		
		// next assume it is a internal providerId (Note this is a person_id 
		// not a user_id) and try again
		try {
			personProvider = Context.getPersonService().getPerson(Integer.parseInt(userName));
			if ( personProvider != null)
				return personProvider.getPersonId();
		}catch (NumberFormatException e) {e.printStackTrace();}
		
		
		// now assume its a systemId without a dash: fix the dash and try again
		if (userName != null && userName.trim() != "") {
			if (userName.indexOf("-") == -1 && userName.length() > 1) {
				userName=userName.substring(0,userName.length()-1) + "-" + userName.substring(userName.length()-1);
				userProvider = Context.getUserService().getUserByUsername(userName);
				if ( userProvider != null)
					return userProvider.getPerson().getPersonId();
			}
		}
		
		return null;
	}
	
	/**
	 * Generate a random alphanumeric string 
	 * @return a random string
	 */
	public static String generateFileName(Date date) {
		// format to print date in filename
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HHmm-ssSSS");
		
		// use current date if none provided
		if (date == null)
			date = new Date();
		
		StringBuilder filename = new StringBuilder();
		
		// the start of the filename is the time so we can do some sorting
		filename.append(dateFormat.format(date));
		
		// the end of the filename is a random number between 0 and 10000
		filename.append((int) (Math.random() * 10000));
		
		return filename.toString();
	}
	
	/**
	 * Deletes a file specified by form path
	 */
	public static void deleteFile(String filePath){
		try{
			if(filePath != null){
				File fileToDelete=new File(filePath);
				
				//delete the file
				fileToDelete.delete();
			}
		}
		catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}
}