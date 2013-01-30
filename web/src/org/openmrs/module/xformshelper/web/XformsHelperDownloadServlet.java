package org.openmrs.module.xformshelper.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.xforms.XformConstants;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.openmrs.module.xformshelper.XformsHelperXform;

/**
 * Provides xformshelper download services.
 * 
 * @author Samuel Mbugua
 */
public class XformsHelperDownloadServlet extends HttpServlet {
	public static final long serialVersionUID = 123427878377111L;
	private Log log = LogFactory.getLog(this.getClass());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		XformsHelperService xfhs = (XformsHelperService) Context.getService(XformsHelperService.class);
		String strformId = request.getParameter("formId");
		String fileName = request.getParameter("file");
		if ("odk_clinic".equals(request.getParameter("type"))) {
			try {
				Integer formId = Integer.parseInt(strformId);
				XformsHelperXform xform = xfhs.getDownloadableXformByFormId(formId);
				response.setHeader(XformConstants.HTTP_HEADER_CONTENT_DISPOSITION, XformConstants.HTTP_HEADER_CONTENT_DISPOSITION_VALUE + xform.getXformName());
				response.setCharacterEncoding(XformConstants.DEFAULT_CHARACTER_ENCODING);
				response.getWriter().print(xform.getXformXml());
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}else {
			//Here we hope that string fileName is not null
			try {
				XformsHelperXform xform = xfhs.getDownloadableXformByName(fileName);
				response.setHeader(XformConstants.HTTP_HEADER_CONTENT_DISPOSITION, XformConstants.HTTP_HEADER_CONTENT_DISPOSITION_VALUE + xform.getXformName());
				response.setCharacterEncoding(XformConstants.DEFAULT_CHARACTER_ENCODING);
				response.getWriter().print(xform.getXformXml());
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
	}
}