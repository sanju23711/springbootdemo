package com.thg.gdeaws.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("hibernateUtil")
public class HibernateUtil {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isReady(){
		boolean ready = sessionFactory != null;
		if(ready) ready = sessionFactory.getCurrentSession() != null;
		if(ready) ready = sessionFactory.getCurrentSession().isConnected();
		return ready;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Date getTimeStamp(){
		return sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<Date>() {
			@Override
			public Date execute(Connection conn) throws SQLException {
				Statement stmt = conn.createStatement( );
				ResultSet rs =  stmt.executeQuery("select 1 as result, sysdate from dual");
				Date date = null;
				if(rs.next()) date = rs.getTimestamp("sysdate");
				//rs =  stmt.executeQuery("select * from all_tables where table_name like '%THGACO_DE%'");
				//while(rs.next()) 
					//System.out.println(rs.getString("TABLE_NAME"));
				rs.close();
				stmt.close();
				return date;
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	private Map daos = new HashMap();
	
	@SuppressWarnings("unchecked")
	public <T> Dao<T, Long> getDao(Class<T> type) {
		if(!daos.containsKey(type)){
			HibernateDao<T, Long> hibernateDao = new HibernateDao<T, Long>(type);
			hibernateDao.setSessionFactory(sessionFactory);
			daos.put(type, hibernateDao);
		}
		
		return (Dao<T, Long>) daos.get(type);
	}
}
