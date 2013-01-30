package org.openmrs.module.xformshelper.extension.html;

import java.util.Map;
import java.util.TreeMap;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;
import org.openmrs.util.InsertedOrderComparator;

/**
 * Anchor for the module in the Main OpenMRS administration page
 * 
 * @author Samuel Mbugua
 *
 */
@SuppressWarnings("deprecation")
public class XformsHelperAdminExt extends AdministrationSectionExt {
	
	private static String requiredPrivileges = null;
	

	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	

	public String getTitle() {
		return "xformshelper.title";
	}
	
	public String getRequiredPrivilege() {
		if (requiredPrivileges == null) {
			StringBuilder builder = new StringBuilder();
			requiredPrivileges = builder.toString();
		}
		
		return requiredPrivileges;
	}
	
	public Map<String, String> getLinks() {
		
		Map<String, String> map = new TreeMap<String, String>(new InsertedOrderComparator());
		map.put("module/xformshelper/propertiesPage.form", "xformshelper.properties");
		map.put("module/xformshelper/downloadableXforms.list", "xformshelper.downloadableXforms");
		map.put("module/xformshelper/resolveErrors.list", "xformshelper.resolveErrors.title");
		map.put("module/xformshelper/processingWarning.list", "xformshelper.processingWarning.title");
		map.put("module/xformshelper/syncLog.list", "xformshelper.sync.title");
		return map;
	}
	
}
