package cs.com.services;

import cs.com.common.HibernateUtil;
import cs.com.entity.AlertEvent;
import cs.com.entity.JsonError;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;


public class HsqlServiceImpl implements HsqlService{

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HsqlServiceImpl.class);
    private SessionFactory sessionFactory;

    public HsqlServiceImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public HsqlServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveAlertEvent(String id, String type, String host, long duration, boolean alert) {
        AlertEvent alertEvent = new AlertEvent();
        alertEvent.setId(id);
        alertEvent.setDuration(duration);
        alertEvent.setHost(host);
        alertEvent.setType(type);
        alertEvent.setAlert(alert);
        Session session = getSession();
        session.beginTransaction();
        session.save(alertEvent);
        session.getTransaction().commit();
        session.close();
        logger.info("Alert event saved for id : "+id);
    }

    @Override
    public boolean isAlertInDB(String id) {
        Session session = getSession();
        Query query = session.createQuery("from AlertEvent where id= :id");
        query.setString("id", id);
        AlertEvent alertEvent = query.uniqueResult() != null && query.uniqueResult() instanceof  AlertEvent ?
                (AlertEvent) query.uniqueResult() : null;
        session.close();
        if (alertEvent != null) {
            if (!alertEvent.isAlert()) removeAlertEvent(alertEvent);
            return true;
        } else
           return false;
    }

    @Override
    public boolean isJsonErrorInDB(String json) {
        Session session = getSession();
        Query query = session.createQuery("from JsonError where json= :json");
        query.setString("json", json);
        JsonError jsonError = query.uniqueResult() != null && query.uniqueResult() instanceof  JsonError ?
                (JsonError) query.uniqueResult() : null;
        session.close();
        return jsonError != null;
    }

    @Override
    public void saveJsonError(String json, String cause) {
        JsonError jsonError = new JsonError();
        jsonError.setJson(json);
        jsonError.setCause(cause);
        Session session = getSession();
        session.beginTransaction();
        session.save(jsonError);
        session.getTransaction().commit();
        session.close();
        logger.warn("Incorrect JSON line found : "+json);
    }

    @Override
    public void removeAlertEvent(AlertEvent alertEvent) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(alertEvent);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public long getAlertEventsRowCount() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(AlertEvent.class);
        criteria.setProjection(Projections.rowCount());
        long rowCount = criteria.list() != null ? (long) criteria.list().get(0) : 0;
        session.close();
        return rowCount;
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }
}
