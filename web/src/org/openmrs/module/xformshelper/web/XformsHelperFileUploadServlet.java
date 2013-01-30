package org.openmrs.module.xformshelper.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.xformshelper.util.XformsHelperUtil;

/**
 * Provides file upload services to the module
 * 
 * @author Samuel Mbugua
 */

public class XformsHelperFileUploadServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(XformsHelperFileUploadServlet.class);
	private static final long serialVersionUID = 6786000880818376733L;

	/** 
	 * Authenticates in-line user then just delegates to doGet()
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("Upload URL: " + request.getRequestURL());
		String username = request.getParameter("uname");
		String password = request.getParameter("pw");
		if (XformsHelperUtil.authenticate(username, password))
			doGet(request,response);
		else {
			log.warn(this.getClass().getName() + "Error authenticating");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	/**
	 * Receives multipart files and saves them as individual file items on the file system
	 * directory specified by {@link XformsHelperUtil#getXformsHelperDropDir()} 
	 */
	@SuppressWarnings("rawtypes")
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		if (ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
			List fileItemsList = null;
			try {
				fileItemsList = servletFileUpload.parseRequest(request);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			String optionalFileName = "";
			FileItem fileItem = null;
			Iterator iterator = fileItemsList.iterator();
			
			while (iterator.hasNext()) {
				FileItem fileItemTemp = (FileItem) iterator.next();
				if (fileItemTemp.isFormField()) {
					if (fileItemTemp.getFieldName().equals("filename"))
						optionalFileName = fileItemTemp.getString();
				} else
					fileItem = fileItemTemp;
				saveFile(fileItem, optionalFileName);
			}
		}
	}
	
	private static void saveFile(FileItem fileItem, String optionalFileName) {
		if (fileItem != null) {
			String fileName = fileItem.getName();

			/* Save the uploaded file if its size is greater than 0. */
			if (fileItem.getSize() > 0) {
				if (optionalFileName.trim().equals(""))
					fileName = FilenameUtils.getName(fileName);
				else
					fileName = optionalFileName;

				String dirName = XformsHelperUtil.getXformsHelperDropDir().getAbsolutePath() + File.separatorChar;

				File saveTo = new File(dirName + fileName);
				try {
					fileItem.write(saveTo);

				} catch (Exception e) {

				}
			}
		}
	}
}
