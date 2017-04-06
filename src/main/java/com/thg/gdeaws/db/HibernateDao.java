package com.thg.gdeaws.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class HibernateDao<T, ID extends Serializable> implements Dao<T, ID> {
    private Class<T> clazz;
    private SessionFactory sessionFactory;
    private static Logger LOG = Logger.getLogger(HibernateDao.class);

    public HibernateDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id) {
        SessionInfo sessionInfo = getSessionInfo();
		T object = (T) sessionInfo.getSession().get(this.clazz, id);
        sessionInfo.cleanup();
        return object;
    }

    public void save(T object) {
        LOG.debug("----------->" + this.toString());
        SessionInfo sessionInfo = getSessionInfo();
        sessionInfo.getSessionForWriting().saveOrUpdate(object);
        sessionInfo.cleanup();
    }

    public void delete(T object) {
        LOG.debug("----------->" + this.toString());
        SessionInfo sessionInfo = getSessionInfo();
        sessionInfo.getSessionForWriting().delete(object);
        sessionInfo.cleanup();
	}

    public List<T> findAllBy(String property, List<?> values) {
        return (List<T>) findAllBy(property, values, null);
    }

    @SuppressWarnings("unchecked")
	public List<T> findAllBy(String property, List<?> values, Order order) {
        SessionInfo sessionInfo = getSessionInfo();
        Criteria criteria = sessionInfo.getSession().createCriteria(this.clazz)
                .add(Restrictions.in(property, values));
		if (order != null) {
            criteria.addOrder(order);
        }
		List<T> retval = (List<T>) criteria.list();
        sessionInfo.cleanup();
        return retval;
    }

    @SuppressWarnings("serial")
	public List<T> findAllBy(String property, final Object value) {
        LOG.debug("Turning object into list");
		return findAllBy(property, new ArrayList<Object>() {
			{add(value);}
		}, null);
	}

    @SuppressWarnings("serial")
	public List<T> findAllBy(String property, final Object value, Order order) {
        LOG.debug("Turning object into list");
		return findAllBy(property, new ArrayList<Object>() {
			{add(value);}
		}, order);
	}

    public List<T> findAll() {
        SessionInfo sessionInfo = getSessionInfo();
        List<T> retval = findAll(null);
        sessionInfo.cleanup();
        return retval;
    }

    @SuppressWarnings("unchecked")
	public List<T> findAll(Order order) {
        SessionInfo sessionInfo = getSessionInfo();
        Criteria criteria = sessionInfo.getSession().createCriteria(this.clazz);
		if (order != null) {
            criteria.addOrder(order);
        }
		List<T> retval = (List<T>) criteria.list();
        sessionInfo.cleanup();
        return retval;
    }

    public T findBy(String property, final Object value) {
		List<T> results = findAllBy(property, value);
		if (results == null || results.isEmpty())
			return null;
        return results.get(0);
    }

    @SuppressWarnings("unchecked")
	public List<T> findAll(Map<String, Object> values,
			Map<String, String> orders) {
        SessionInfo sessionInfo = getSessionInfo();
        Criteria criteria = sessionInfo.getSession().createCriteria(this.clazz);
		if (values != null && !values.isEmpty()) {
			for (String key : values.keySet()) {
				if ((values.get(key)) instanceof Collection)
					criteria.add(Restrictions.in(key, (Collection<T>) values.get(key)));
				else{
					if(values.get(key) == null)
						criteria.add(Restrictions.isNull(key));
					else
						criteria.add(Restrictions.eq(key, values.get(key)));
				}
			}
		}
		if (orders != null && !orders.isEmpty()) {
			for (String key : orders.keySet()) {
				if ("asc".equalsIgnoreCase(orders.get(key)))
					criteria.addOrder(Order.asc(key));
				if ("desc".equalsIgnoreCase(orders.get(key)))
					criteria.addOrder(Order.desc(key));
			}
		}
		List<T> retval = (List<T>) criteria.list();
        sessionInfo.cleanup();
        return retval;
    }
    
    @SuppressWarnings("unchecked")
	public List<T> findAll(Map<String, String> aliases, Map<String, Object> values,
			Map<String, String> orders) {
        SessionInfo sessionInfo = getSessionInfo();
        Criteria criteria = sessionInfo.getSession().createCriteria(this.clazz);
        if (aliases != null && !aliases.isEmpty()) {
			for (String key : aliases.keySet()) {
				if(aliases.get(key) != null)
					criteria.createAlias(key, aliases.get(key));
			}
		}
		if (values != null && !values.isEmpty()) {
			for (String key : values.keySet()) {
				if ((values.get(key)) instanceof Collection)
					criteria.add(Restrictions.in(key, (Collection<T>) values.get(key)));
				else{
					if(values.get(key) == null)
						criteria.add(Restrictions.isNull(key));
					else
						criteria.add(Restrictions.eq(key, values.get(key)));
				}
			}
		}
		if (orders != null && !orders.isEmpty()) {
			for (String key : orders.keySet()) {
				if ("asc".equalsIgnoreCase(orders.get(key)))
					criteria.addOrder(Order.asc(key));
				if ("desc".equalsIgnoreCase(orders.get(key)))
					criteria.addOrder(Order.desc(key));
			}
		}
		List<T> retval = (List<T>) criteria.list();
        sessionInfo.cleanup();
        return retval;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void flush() {
        this.sessionFactory.getCurrentSession().flush();
    }

    protected SessionInfo getSessionInfo() {
        try {
			return new SessionInfo(this.sessionFactory.getCurrentSession(), false);
        } catch (HibernateException he) {
            LOG.debug(he);
			return new SessionInfo(this.sessionFactory.openSession(), true);
        }
	}
}
