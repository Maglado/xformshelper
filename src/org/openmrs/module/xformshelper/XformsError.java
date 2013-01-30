package org.openmrs.module.xformshelper;

import java.util.Date;

import org.openmrs.Auditable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.User;

/**
 * Error object, stores errors that occur during remote xforms processing
 * @author  Samuel Mbugua
 *
 */
public class XformsError extends BaseOpenmrsObject implements Auditable{

	private Integer xformsErrorId;
	private String formName;
	private String error;
	private String errorDetails;
	private String comment;
	private User commentedBy;
	private Date dateCommented;
	private User creator;
	private Date dateCreated;

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

	/**
	 * @return Returns the error.
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            The error to set.
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return Returns the errorDetails.
	 */
	public String getErrorDetails() {
		return errorDetails;
	}

	/**
	 * @param errorDetails
	 *            The errorDetails to set.
	 */
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	/**
	 * @return Returns the formName.
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * @param formName
	 *            The formData to set.
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @return Returns the xformsErrorId.
	 */
	public Integer getXformsErrorId() {
		return xformsErrorId;
	}

	/**
	 * @param xformsErrorId
	 *            The xformsErrorId to set.
	 */
	public void setXformsErrorId(Integer xformsErrorId) {
		this.xformsErrorId = xformsErrorId;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the commentedBy
	 */
	public User getCommentedBy() {
		return commentedBy;
	}

	/**
	 * @param commentedBy the commentedBy to set
	 */
	public void setCommentedBy(User commentedBy) {
		this.commentedBy = commentedBy;
	}

	/**
	 * @return the dateCommented
	 */
	public Date getDateCommented() {
		return dateCommented;
	}

	/**
	 * @param dateCommented the dateCommented to set
	 */
	public void setDateCommented(Date dateCommented) {
		this.dateCommented = dateCommented;
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
		return this.getXformsErrorId();
	}

	public void setId(Integer id) {
		this.setXformsErrorId(id);
	}

}
