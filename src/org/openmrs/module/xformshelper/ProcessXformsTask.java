package org.openmrs.module.xformshelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * A task to process forms in the remote-xforms queue.
 * 
 * @author Samuel Mbugua
 */
public class ProcessXformsTask  extends AbstractTask {

	private static Log log = LogFactory.getLog(ProcessXformsTask.class);

	// An instance of the xforms helper processor.
	private XformsHelperProcessor processor = null;

	/**
	 * Default Convenience constructor 
	 */
	public ProcessXformsTask() {
		if (processor == null)
			processor = new XformsHelperProcessor();
	}

	/**
	 * Process the each xform in the queue and remove it from this queue
	 */
	public void execute() {
		Context.openSession();
		log.debug("Running xformshelper task... ");
		try {
			if (Context.isAuthenticated() == false)
				authenticate();
			processor.processXforms();
		} catch (APIException e) {
			log.error("Error running xformshelper forms task", e);
			throw e;
		} finally {
			Context.closeSession();
		}
	}

	/*
	 * Resources clean up
	 */
	public void shutdown() {
		processor = null;
		super.shutdown();
		log.debug("Shutting down ProcessXforms task ...");
	}
}