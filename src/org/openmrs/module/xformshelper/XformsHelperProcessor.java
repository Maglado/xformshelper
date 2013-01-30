package org.openmrs.module.xformshelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Samuel Mbugua
 */

public class XformsHelperProcessor {

	private static final Log log = LogFactory.getLog(XformsHelperProcessor.class);
	private XformsHelperSplitProcessor splitProcessor = null;
	private XformsHelperRawFormsProcessor queueProcessor = null;
	private XformsHelperQueueProcessor uploadProcessor = null;

	public void processXforms() {
		log.debug("XformsHelper: Processing xforms");
				
			if (queueProcessor == null)
				queueProcessor = new XformsHelperRawFormsProcessor();
			queueProcessor.processRawFormsQueue();
	
			// Split submitted xforms
			if (splitProcessor == null)
				splitProcessor = new XformsHelperSplitProcessor();
			splitProcessor.splitForms();
			
			// Upload patients to xforms module for processing
			if (uploadProcessor == null)
				uploadProcessor = new XformsHelperQueueProcessor();
			uploadProcessor.processXformsHelperQueue();
	
		}
	}
