package org.openmrs.module.xformshelper;

/**
 * A model that expresses a complex xml in a user friendly 
 * way to allow for editing
 * @author Samuel Mbugua
 *
 */
public class XformEditModel {
	private String nodeName;
	private String nodeValue;
	private String nodeAttributes;
	
	
	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}
	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	/**
	 * @return the nodeValue
	 */
	public String getNodeValue() {
		return nodeValue;
	}
	/**
	 * @param nodeValue the nodeValue to set
	 */
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}
	/**
	 * @return the nodeAttributes
	 */
	public String getNodeAttributes() {
		return nodeAttributes;
	}
	/**
	 * @param nodeAttributes the nodeAttributes to set
	 */
	public void setNodeAttributes(String nodeAttributes) {
		this.nodeAttributes = nodeAttributes;
	}
	
}