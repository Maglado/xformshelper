package org.openmrs.module.xformshelper;

import java.util.Date;

import org.openmrs.Auditable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.odkconnector.api.ConceptConfiguration;
import org.openmrs.module.odkconnector.api.service.ConnectorService;

/**
 * Xform object, stores downloadable forms
 * @author  Samuel Mbugua
 *
 */
public class XformsHelperXform extends BaseOpenmrsObject implements Auditable{
	
	private Integer xformsHelperXformId;
	private Integer xformId;
	private String xformName;
	private String xformMeta;
	private String xformXml;
	private Integer program;
	private ConceptConfiguration programConcept;
	private User creator;
	private Date dateCreated;

	/**
	 * @return the xformHelperXformId
	 */
	public Integer getXformsHelperXformId() {
		return xformsHelperXformId;
	}

	/**
	 * @param xformsHelperXformId the xformsHelperXformId to set
	 */
	public void setXformsHelperXformId(Integer xformsHelperXformId) {
		this.xformsHelperXformId = xformsHelperXformId;
	}

	/**
	 * @return the xformId
	 */
	public Integer getXformId() {
		return xformId;
	}

	/**
	 * @param xformId the xformId to set
	 */
	public void setXformId(Integer xformId) {
		this.xformId = xformId;
	}

	/**
	 * @return the xformName
	 */
	public String getXformName() {
		return xformName;
	}

	/**
	 * @param xformName the xformName to set
	 */
	public void setXformName(String xformName) {
		this.xformName = xformName;
	}

	/**
	 * @return the xformMeta
	 */
	public String getXformMeta() {
		return xformMeta;
	}

	/**
	 * @param xformMeta the xformMeta to set
	 */
	public void setXformMeta(String xformMeta) {
		this.xformMeta = xformMeta;
	}

	/**
	 * @return the xformXml
	 */
	public String getXformXml() {
		return xformXml;
	}

	/**
	 * @param xformXml the xformXml to set
	 */
	public void setXformXml(String xformXml) {
		this.xformXml = xformXml;
	}

	/**
	 * @return the program
	 */
	public Integer getProgram() {
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(Integer program) {
		this.program = program;
	}

	/**
	 * @return the programConcept
	 */
	public ConceptConfiguration getProgramConcept() {
		if (this.program != null){
			//create a program object
			ConnectorService cs = Context.getService(ConnectorService.class);
			programConcept=cs.getConceptConfiguration(program);
		}
		return programConcept;
	}

	/**
	 * @param programConcept the programConcept to set
	 */
	public void setProgramConcept(ConceptConfiguration programConcept) {
		this.programConcept = programConcept;
	}

	/**
	 * @return Returns the creator.
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            The creator to set.
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return Returns the dateCreated.
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            The dateCreated to set.
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public User getChangedBy() {
		return null;
	}

	public Date getDateChanged() {
		return null;
	}

	public void setChangedBy(User changedBy) {}

	public void setDateChanged(Date dateChanged) {}

	public Integer getId() {
		return this.getXformsHelperXformId();
	}

	public void setId(Integer id) {
		this.setXformsHelperXformId(id);
	}

}
