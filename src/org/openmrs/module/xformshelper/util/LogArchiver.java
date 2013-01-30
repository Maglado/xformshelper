package org.openmrs.module.xformshelper.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Archiver for logs 
 * 
 * @author Samuel Mbugua
 *
 */
public class LogArchiver {
	private static final Log log = LogFactory.getLog(LogArchiver.class);

	private static File getArchiveFile(String logType) {
		File archiveDir=XformsHelperUtil.getXformsHelperArchiveDir(new Date());
		String archiveFileName=archiveDir.getAbsolutePath() + File.separator + logType + "-archive-" 
				+ XformsHelperUtil. generateFileName(new Date()) + ".log";
		File archiveFile = new File(archiveFileName);
		if (!archiveFile.exists())
			try {
				log.debug("Creating new archive log file");
				archiveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return archiveFile;
	}
	
	private static void writer(String filePath, String line) {
		try
		{
		    FileWriter fileWriter = new FileWriter(filePath,true);
		    fileWriter.write(line);
		    fileWriter.close();
		}
		catch(IOException e){e.printStackTrace();} 
	}
	
	public static boolean createArchiveLog(String outPutLine, String logType){
		try {
			File archiveLogFile = getArchiveFile(logType);
			writer(archiveLogFile.getAbsolutePath(),outPutLine);
			return true;
		}
		catch (Throwable t) {
			log.error("Cannot create log file, probably an empty file");
			return false;
		}
	}
}