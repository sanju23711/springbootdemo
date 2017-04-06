package com.thg.gdeaws.db;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionInfo {
    private Session session;
    private boolean needsTransaction;
    private boolean closeSession;
    private Transaction tx;
    
    private static final Logger LOG = Logger.getLogger(SessionInfo.class);
    
    public SessionInfo(Session session, boolean needsClosing) {
        this(session, needsClosing, needsClosing);
    }
    
    public SessionInfo(Session session, boolean transact, boolean closeSession) {
        this.session = session;
        this.needsTransaction = transact;
        this.closeSession = closeSession;
        if (this.needsTransaction) { this.tx = this.session.beginTransaction(); }
    }
    public Session getSession() {
        return this.session;
    }
    
    public Session getSessionForWriting() {
        if (this.session.getTransaction() == null || !this.session.getTransaction().isActive()) {
            this.tx = this.session.beginTransaction();
            this.needsTransaction = true;
        }
        return this.session;
    }
  

    
    public void cleanup() {
        if (this.needsTransaction) {
            try {this.tx.commit(); } catch (HibernateException he) { LOG.warn("Error commiting transaction", he); }
        }
        if (this.closeSession) {
            try {this.session.close(); } catch (HibernateException he) { LOG.warn("Error closing session", he); }
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        cleanup();
        super.finalize();
    }
    
}
