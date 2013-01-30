package org.openmrs.module.xformshelper.db;

import java.util.List;

import org.openmrs.module.xformshelper.XformsError;
import org.openmrs.module.xformshelper.XformsHelperXform;

/**
 * Public Interface to the HibernateXformsHelperDAO
 * 
 * @author Samuel Mbugua
 */
public interface XformsHelperDAO {
	
	public void saveErrorInDatabase(XformsError xformsError);

	public List<XformsError> getAllXformsErrors();

	public XformsError getErrorById(Integer errorId);

	public void deleteError(XformsError error);

	public List<XformsHelperXform> getAllDownloadableXforms();

	public void saveDownloadableXform(XformsHelperXform xform);

	public XformsHelperXform getDownloadableXform(Integer xformsHelperXformId);

	public void deleteDownloadableXform(XformsHelperXform xformsHelperXform);

	public XformsHelperXform getDownloadableXformByFormId(Integer xformId);

	public XformsHelperXform getDownloadableXformByName(String formName);

	public List<XformsError> getErrorsByFormName(String formName);

	public List<XformsHelperXform> getDownloadableXformsByProgram(int program);
}