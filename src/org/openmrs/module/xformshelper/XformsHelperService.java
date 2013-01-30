package org.openmrs.module.xformshelper;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.springframework.transaction.annotation.Transactional;

/**
 * Required methods for any xform helper service
 * 
 * @author  Samuel Mbugua
 */
@Transactional
public interface XformsHelperService {
	
	/**
	 * A getter for all downloadable forms uploaded into the module
	 * @return List of resource files
	 */
	public List<XformsHelperXform> getAllDownloadableXforms();
	
	/**
	 * Get an xform queue given the absolute path to an xml file
	 * @param absoluteFilePath
	 * @return {@link XformsQueue}
	 */
	public XformsQueue getXformsHelperQueue(String absoluteFilePath);

	//ERRORS RELATED METHODS
	/**
	 * Get all errors logged in the error table
	 * @return list of {@link XformsError}
	 */
	public List<XformsError> getAllXformsErrors();
	
	/**
	 * Get a specific  error by a specified id
	 * @param errorId
	 * @return {@link XformsError}
	 */
	public XformsError getErrorById(Integer errorId);
	
	/**
	 * Create a new error record in the error table
	 * @param xformsHelperError
	 */
	public void saveErrorInDatabase(XformsError xformsHelperError);
	
	/** Delete a specified error from the database
	 * @param error
	 */
	public void deleteError(XformsError error);

	/**
	 * Generate sync logs for a specific date. If no logs return null
	 * @param logDate
	 * @return List of sync logs
	 */
	public List<SyncLogModel> getSyncLog(Date logDate);
	
	/**
	 * Get all date encoded sync log files. If none return null
	 * @return List of sync log files
	 */
	public List<String> getAllSyncLogs();
	
	public SortedMap<String, String> getSystemVariables();

	public void saveDownloadableXform(XformsHelperXform xform);

	public XformsHelperXform getDownloadableXform(Integer xformsHelperXformId);
	
	public XformsHelperXform getDownloadableXformByFormId(Integer xformId);

	public void deleteDownloadableXform(XformsHelperXform xformsHelperXform);

	public XformsHelperXform getDownloadableXformByName(String formName);

	public List<XformsHelperXform> getDownloadableXformsByProgram(int program);
}