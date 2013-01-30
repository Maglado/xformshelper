package org.openmrs.module.xformshelper.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.xformshelper.SyncLogModel;
import org.openmrs.module.xformshelper.XformsError;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.openmrs.module.xformshelper.XformsHelperXform;
import org.openmrs.module.xformshelper.XformsQueue;
import org.openmrs.module.xformshelper.db.XformsHelperDAO;
import org.openmrs.module.xformshelper.util.XformsHelperUtil;

/**
 * @author Samuel Mbugua
 *
 */
public class XformsHelperServiceImpl implements XformsHelperService {
	private static Log log = LogFactory.getLog(XformsHelperServiceImpl.class);
	
	private XformsHelperDAO dao;
	
	public XformsHelperServiceImpl() {
	}
	
	@SuppressWarnings("unused")
	private XformsHelperDAO getXformsHelperDAO() {
		return dao;
	}
	
	public void setXformsHelperDAO(XformsHelperDAO dao) {
		this.dao = dao;
	}
	

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getXformsHelperQueue(java.lang.String)
	 */
	public XformsQueue getXformsHelperQueue(String absoluteFilePath) {
		XformsQueue queueItem = new XformsQueue();
		queueItem.setFileSystemUrl(absoluteFilePath);
		log.debug(absoluteFilePath);
		return queueItem;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getSystemVariables()
	 */
	public SortedMap<String,String> getSystemVariables() {
		TreeMap<String, String> systemVariables = new TreeMap<String, String>();
		systemVariables.put("XFORMS_DROP_DIR", XformsHelperUtil.getXformsHelperDropDir().getAbsolutePath());
		systemVariables.put("XFORMS_QUEUE_DIR", XformsHelperUtil.getXformsHelperQueueDir().getAbsolutePath());
		systemVariables.put("XFORMS_ARCHIVE_DIR", XformsHelperUtil.getXformsHelperArchiveDir(null).getAbsolutePath());
		systemVariables.put("XFORMS_ERROR_DIR", XformsHelperUtil.getXformsHelperErrorDir().getAbsolutePath());
		return systemVariables;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getAllDownloadableXforms()
	 */
	public List<XformsHelperXform> getAllDownloadableXforms() {
		return dao.getAllDownloadableXforms();
	}
	

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#saveDownloadableXform(org.openmrs.module.xformshelper.XformsHelperXform)
	 */
	public void saveDownloadableXform(XformsHelperXform xform) {
		dao.saveDownloadableXform(xform);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#saveErrorInDatabase(org.openmrs.module.xformshelper.XformsError)
	 */
	public void saveErrorInDatabase(XformsError xformsError) {
		// first get an error in the database based on similar formName.
		dao.saveErrorInDatabase(xformsError);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getDownloadableXform(java.lang.Integer)
	 */
	public XformsHelperXform getDownloadableXform(Integer xformsHelperXformId) {
		return dao.getDownloadableXform(xformsHelperXformId);
	}
	

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getDownloadableXformByFormId(java.lang.Integer)
	 */
	public XformsHelperXform getDownloadableXformByFormId(Integer xformId) {
		return dao.getDownloadableXformByFormId(xformId);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getDownloadableXformName(java.lang.String)
	 */
	public XformsHelperXform getDownloadableXformByName(String formName) {
		return dao.getDownloadableXformByName(formName);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getDownloadableXformsByProgram(int)
	 */
	public List<XformsHelperXform> getDownloadableXformsByProgram(int program) {
		return dao.getDownloadableXformsByProgram(program);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#deleteDownloadableXform(org.openmrs.module.xformshelper.XformsHelperXform)
	 */
	public void deleteDownloadableXform(XformsHelperXform xformsHelperXform) {
		dao.deleteDownloadableXform(xformsHelperXform);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getAllXformsErrors()
	 */
	public List<XformsError> getAllXformsErrors() {
		return dao.getAllXformsErrors();
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getErrorById(java.lang.Integer)
	 */
	public XformsError getErrorById(Integer errorId) {
		return dao.getErrorById(errorId);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#deleteError(org.openmrs.module.xformshelper.XformsError)
	 */
	public void deleteError(XformsError error) {
		dao.deleteError(error);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getSyncLog(java.util.Date)
	 */
	public List<SyncLogModel> getSyncLog(Date logDate) {
		List<SyncLogModel> logList = new ArrayList<SyncLogModel>();
		File logDir=XformsHelperUtil.getXformsHelperSyncLogDir();
		if (logDate == null)
			logDate=new Date();
		String logFileName = logDir.getAbsolutePath() + File.separator + "log-" + new SimpleDateFormat("yyyy-MM-dd").format(logDate) + ".log";
		File logFile = new File(logFileName);
		if (!logFile.exists())
			return null;			
		String line = null;
		try {
			BufferedReader input =  new BufferedReader(new FileReader(logFile));
			try {
				while (( line = input.readLine()) != null){
					if (line.indexOf(",")!=-1) {
						SyncLogModel logModel = getLogModel(line);
						if (logModel != null)
							logList.add(logModel);
					}
				}
			}
			finally {
				 input.close();
			 }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
		return logList;
	}
	
	/**
	 * Takes a Comma Separated line and creates an object of type {@link SyncLogModel}
	 */
	private static SyncLogModel getLogModel(String line) {
		SyncLogModel syncLogModel = new SyncLogModel();
		// syncId
		if (line.indexOf(",") != -1) {
			syncLogModel.setSyncId(Integer.parseInt(line.substring(0,line.indexOf(","))));
			line=line.substring(line.indexOf(",") + 1);
		}else
			return null;
		// syncDate
		if (line.indexOf(",") != -1) {
			try {
				DateFormat df =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				syncLogModel.setSyncDate(df.parse(line.substring(0,line.indexOf(","))));
				line=line.substring(line.indexOf(",") + 1);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}else
			return null;
		
		// providerId
		if (line.indexOf(",") != -1) {
			syncLogModel.setProviderId(line.substring(0,line.indexOf(",")));
			line=line.substring(line.indexOf(",") + 1);
		}else
			return null;
		
		// deviceId
		if (line.indexOf(",") != -1) {
			syncLogModel.setDeviceId(line.substring(0,line.indexOf(",")));
			line=line.substring(line.indexOf(",") + 1);
		}else
			return null;
		
		// 	householdId;
		if (line.indexOf(",") != -1) {
			syncLogModel.setHouseholdId(line.substring(0,line.indexOf(",")));
			line=line.substring(line.indexOf(",") + 1);
		}else
			return syncLogModel;
		
		// fileName;
		if (line.indexOf(",") != -1) {
			syncLogModel.setFileName(line.substring(0,line.indexOf(",")));
			line=line.substring(line.indexOf(",") + 1);
		}else
			return syncLogModel;
		
		// fileSize;
		if (line.indexOf(",") != -1) {
			syncLogModel.setFileSize(line.substring(0,line.indexOf(",")));
			line=line.substring(line.indexOf(",") + 1);
		}else
			syncLogModel.setFileSize(line);
		
		return syncLogModel;
	
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.xformshelper.XformsHelperService#getAllSyncLogs()
	 */
	public List<String> getAllSyncLogs() {
		List<String> logFiles = new ArrayList<String>();
		List<Date> logDates = new ArrayList<Date>();
		File logDir=XformsHelperUtil.getXformsHelperSyncLogDir();
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd");
		for (File file : logDir.listFiles()) {
			String fileName=file.getName();
			if (fileName.indexOf("-") != -1 && fileName.indexOf(".") != -1) {
				try {
					fileName=fileName.substring(fileName.indexOf("-")+1,fileName.lastIndexOf("."));
					Date date = df.parse(fileName);
					logDates.add(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if (!logDates.isEmpty()) {
			df =new SimpleDateFormat("yyyy-MMM-dd");
			DateCompare compare = new DateCompare();
			Collections.sort(logDates, compare);
			for (Date date : logDates)
				logFiles.add(df.format(date));
		}
		return logFiles;
	}
	
	class DateCompare implements Comparator<Date> {
		public int compare(Date one, Date two) {
			return two.compareTo(one);
		}
	}
}