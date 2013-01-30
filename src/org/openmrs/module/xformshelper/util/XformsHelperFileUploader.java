package org.openmrs.module.xformshelper.util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.openmrs.module.xforms.download.XformDataUploadManager;
import org.openmrs.util.OpenmrsUtil;

/**
 * Uploads patient forms from this module to the xforms module
 * 
 * @author Samuel Mbugua
 */
public class XformsHelperFileUploader {
	
	public static void submitXFormFile(String filePath, String enterer) throws IOException, Exception {
		XformDataUploadManager.processXform(OpenmrsUtil.getFileAsString(new File(filePath)), getRandomSessionString(), enterer,true,null);
	}
	
	/**
	 * Generate a random alphanumeric session string 
	 * @return a random string
	 */
	private static String getRandomSessionString() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}