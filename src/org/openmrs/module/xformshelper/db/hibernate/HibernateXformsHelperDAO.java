package org.openmrs.module.xformshelper.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.openmrs.module.xformshelper.XformsError;
import org.openmrs.module.xformshelper.XformsHelperXform;
import org.openmrs.module.xformshelper.db.XformsHelperDAO;

/**
 * Database interface for the module 
 * 
 * @author Samuel Mbugua
 *
 */
public class HibernateXformsHelperDAO implements XformsHelperDAO {

	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Default public constructor
	 */
	public HibernateXformsHelperDAO() { }
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @see org.openmrs.module.xformshelper.db.XformsHelperDAO#saveErrorInDatabase(org.openmrs.module.xformshelper.XformsError)
	 */
	public void saveErrorInDatabase(XformsError xformsError) {
		sessionFactory.getCurrentSession().saveOrUpdate(xformsError);
	}

	/**
	 * @see org.openmrs.module.xformshelper.db.XformsHelperDAO#getAllXformsErrors()
	 */
	@SuppressWarnings("unchecked")
	public List<XformsError> getAllXformsErrors() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsError.class);
		return (List<XformsError>) criteria.list();
	}

	/**
	 * @see org.openmrs.module.xformshelper.db.XformsHelperDAO#getErrorById(java.lang.Integer)
	 */
	public XformsError getErrorById(Integer errorId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsError.class);
		criteria.add(Expression.like("xformsErrorId", errorId));
		return (XformsError) criteria.uniqueResult();
	}

	/**
	 * @see org.openmrs.module.xformshelper.db.XformsHelperDAO#deleteError(org.openmrs.module.xformshelper.XformsError)
	 */
	public void deleteError(XformsError error) {
		sessionFactory.getCurrentSession().delete(error);
	}
	
	/**
	 * @see org.openmrs.module.xformshelper.db.XformsHelperDAO#getAllDownloadableXforms()
	 */
	@SuppressWarnings("unchecked")
	public List<XformsHelperXform> getAllDownloadableXforms() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsHelperXform.class);
		return (List<XformsHelperXform>) criteria.list();
	}

	@Override
	public void saveDownloadableXform(XformsHelperXform xform) {
		sessionFactory.getCurrentSession().saveOrUpdate(xform);
	}

	@Override
	public XformsHelperXform getDownloadableXform(Integer xformsHelperXformId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsHelperXform.class);
		criteria.add(Expression.like("xformsHelperXformId", xformsHelperXformId));
		return (XformsHelperXform) criteria.uniqueResult();
	}

	@Override
	public XformsHelperXform getDownloadableXformByFormId(Integer xformId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsHelperXform.class);
		criteria.add(Expression.like("xformId", xformId));
		return (XformsHelperXform) criteria.uniqueResult();
	}
	
	@Override
	public void deleteDownloadableXform(XformsHelperXform xformsHelperXform) {
		sessionFactory.getCurrentSession().delete(xformsHelperXform);
	}

	@Override
	public XformsHelperXform getDownloadableXformByName(String formName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsHelperXform.class);
		criteria.add(Expression.like("xformName", formName));
		return (XformsHelperXform) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<XformsHelperXform> getDownloadableXformsByProgram(int program) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsHelperXform.class);
		criteria.add(Expression.like("program", program));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XformsError> getErrorsByFormName(String formName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(XformsError.class);
		criteria.add(Expression.like("formName", formName));
		return criteria.list();
	}
}