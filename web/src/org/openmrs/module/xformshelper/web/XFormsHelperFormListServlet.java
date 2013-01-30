package org.openmrs.module.xformshelper.web;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.xforms.XformConstants;
import org.openmrs.module.xformshelper.XformsHelperService;
import org.openmrs.module.xformshelper.XformsHelperXform;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Creates an formList XMl from which a phone will get resources
 * 
 * @author Samuel Mbugua
 */

public class XFormsHelperFormListServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(XFormsHelperFormListServlet.class);
	private static final long serialVersionUID = 6786000880818376733L;
	private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder docBuilder;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String programParameter = request.getParameter("program");
		int program;
		
		try {
			program = Integer.valueOf(programParameter);
		} catch (NumberFormatException e){
			program =0;
		}
		
		try {
			
			XformsHelperService xfhs = Context.getService(XformsHelperService.class);
			
			List<XformsHelperXform> xformsList;
			if (program != 0)
				xformsList = xfhs.getDownloadableXformsByProgram(program);
			else
				xformsList = xfhs.getAllDownloadableXforms();
			
			docBuilder = docBuilderFactory.newDocumentBuilder();

			String xml = "<?xml version='1.0' encoding='UTF-8' ?>";
			if("odk_clinic".equalsIgnoreCase(request.getParameter("type"))){
				xml += "\n<xforms>";
				for (XformsHelperXform xformsHelperXform : xformsList) {
					Document doc = docBuilder.parse(IOUtils.toInputStream(xformsHelperXform.getXformXml()));
					NodeList nodeList = doc.getElementsByTagName("form");
					Node rootNode= nodeList.item(0);
					NamedNodeMap nodeMap=rootNode.getAttributes();
					String formId = nodeMap.getNamedItem("id").getNodeValue();
					String fileName = xformsHelperXform.getXformName();
					fileName=formatXmlString(fileName);
					xml += "\n  <xform>";
					xml += "\n <id>" + formId + "</id>";
					xml += "\n <name>" + fileName.replace('_', ' ').substring(0,fileName.lastIndexOf(".")) + "</name>";
					xml += "</xform>";
				}
				xml += "\n</xforms>";
			} else {
				xml += "\n<forms>";
				for (XformsHelperXform xformsHelperXform : xformsList) {
					String fileName = xformsHelperXform.getXformName();
					fileName=formatXmlString(fileName);
					String url = request.getRequestURL().toString();
					String fileUrl = url.substring(0, url.lastIndexOf('/') + 1);
					fileUrl += "xfhFormDownload?file=";
					xml += "\n  <form ";
					xml += "url=\"" + fileUrl + fileName + "\">";
					xml += fileName.replace('_', ' ').substring(0,fileName.lastIndexOf("."));
					xml += "</form>";
				}
				xml += "\n</forms>";
			}
			response.setContentType(XformConstants.HTTP_HEADER_CONTENT_TYPE_XML);
			response.getOutputStream().print(xml);
		}

		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private String formatXmlString(String aString){
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(aString);
		char character =  iterator.current();
		while (character != CharacterIterator.DONE ){
			if (character == '<') {
				result.append("&lt;");
			}
			else if (character == '>') {
				result.append("&gt;");
			}
			else if (character == '\"') {
				result.append("&quot;");
			}
			else if (character == '\'') {
				//result.append("&#039;");
				//result.append("&apos;");
				result.append(character);
			}
			else if (character == '&') {
				result.append("&amp;");
			}
			else {
				//the char is not a special one
				//add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
}