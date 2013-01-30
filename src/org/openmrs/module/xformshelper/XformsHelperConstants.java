package org.openmrs.module.xformshelper;

/**
 * Module wide constants are kept here
 * 
 * @author Samuel Mbugua
 * 
 */
public class XformsHelperConstants {
	
	/** xforms-helper-sync-log directory. */
	public final static String GP_XFORMS_HELPER_SYNC_LOG_DIR_DEFAULT = "xformshelper/synclog";
	
	/** 
	  * Name of the global property for the directory where remote devices sends forms.
	 */
	public final static String GP_XFORMS_HELPER_DROP_DIR = "xformshelper.drop_dir";

	/** The default xforms-helper-drop  directory. */
	public final static String GP_XFORMS_HELPER_DROP_DIR_DEFAULT = "xformshelper/drop_dir";
	
	/**
	 * Name of the global property for the directory for queuing patient forms
	 * before they are processed.
	 */
	public final static String GP_XFORMS_HELPER_QUEUE_DIR = "xformshelper.queue_dir";

	/** The default xforms-helper queue directory. */
	public final static String GP_XFORMS_HELPER_QUEUE_DIR_DEFAULT = "xformshelper/queue";

	/**
	 * Name of the global property for the directory where to put forms that
	 * erred during processing
	 */
	public final static String GP_XFORMS_HELPER_ERROR_DIR = "xformshelper.error_dir";

	/** The default xforms-helper error directory. */
	public final static String GP_XFORMS_HELPER_ERROR_DIR_DEFAULT = "xformshelper/error";
	
	//TODO:
	/*
	 * I do not believe this is the best way since the xform module will archive
	 * the files as well as the formentry module, this could be a table entry
	 * with just form names that can later be retrieved from the xform archive
	 * directory
	 */
	public final static String GP_XFORMS_HELPER_ARCHIVE_DIR = "xformshelper.archive_dir";

	/** The default xforms-helper archive directory. */
	public final static String GP_XFORMS_HELPER_ARCHIVE_DIR_DEFAULT = "xformshelper/archive/%Y/%M";
	
	/** Xforms-helper-pending split directory. */
	public final static String GP_XFORMS_HELPER_PENDING_SPLIT_DIR = "xformshelper/pending_split";
	
	/** Xforms-helper post processing directory. */
	public final static String GP_XFORMS_HELPER_POST_PROCESS_DIR = "xformshelper/post_process";
	
	/** Primary- Data type Selector. */
	public final static String GP_PROCESS_HOUSEHOLD = "xformshelper.processHousehold";
	
	/** The text/xml http content type. */
	public final static String HTTP_HEADER_CONTENT_TYPE_XML = "text/xml; charset=utf-8";
	
	/** The metadata nodes prefix. */
	public static final String METADATA_PREFIX = "/form/meta";

	public final static String META_START_TIME = "start_time";
	public final static String META_END_TIME = "end_time";
	public final static String META_DEVICE_ID = "device_id";
	public final static String META_SUBSCRIBER_ID= "subscriber_id";
	
	public final static String PATIENT_NODE = "/form/patient";
	public final static String PATIENT_ID = "patient.patient_id";
	public final static String PATIENT_IDENTIFIER = "patient.medical_record_number";
	public final static String PATIENT_IDENTIFIER_TYPE = "patient_identifier.identifier_type_id";
	public final static String PATIENT_FAMILYNAME = "patient.family_name";
	public final static String PATIENT_GIVENNAME = "patient.given_name";
	public final static String PATIENT_MIDDLENAME = "patient.middle_name";
	public final static String PATIENT_BIRTHDATE = "patient.birthdate";
	public final static String PATIENT_SEX = "patient.sex";
	public final static String ENCOUNTER_NODE = "/form/encounter";
	public final static String ENCOUNTER_PROVIDER = "encounter.provider_id";
	
	//PRIVILEGES
	public static final String PRIV_RESOLVE_XFORM_ERROR = "Resolve xform Error";
	public static final String PRIV_COMMENT_ON_XFORM_ERRORS = "Comment on xform Errors";
	public static final String PRIV_VIEW_XFORM_ERROR = "View xform Errors";
	public static final String PRIV_VIEW_XFORM_PROPERTY = "View xform Properties";
}
