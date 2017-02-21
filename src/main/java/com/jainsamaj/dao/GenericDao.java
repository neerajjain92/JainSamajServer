package com.jainsamaj.dao;

import com.jainsamaj.model.Users;
import com.jainsamaj.util.DBUtil;
import com.jainsamaj.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by jaine03 on 28/01/17.
 */
public class GenericDao {

    private static Logger logger= LoggerFactory.getLogger(GenericDao.class);

    public <T> T getEntityByProperty(Map<String,Object> property, Class<T> type) {
        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(type);
            criteria.setMaxResults(1);
            property.forEach((key,value) -> {
                criteria.add(Restrictions.eq(key,value));
            });
            List  list = criteria.list();
            tx.commit();
            tx = null;
            if(list!= null && list.size() > 0){
                return type.cast(list.get(0));
            }
        }catch (Exception e){
            e.printStackTrace();
            DBUtil.rollBackTransaction(tx);
        }finally {
            DBUtil.closeSession(session);
        }
        return null;
    }

    public Long saveEntity(Object obj) throws Exception{
        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            tx = session.beginTransaction();
            Serializable generatedId = session.save(obj);
            session.flush();
            tx.commit();
            tx = null;
            if(generatedId!=null){
                return (Long) generatedId;
            }else {
                throw new Exception("Error In Creating Entity");
            }
        }catch (Exception e){
            e.printStackTrace();
            DBUtil.rollBackTransaction(tx);
            throw new Exception("Error while Saving Entity");
        }finally {
            DBUtil.closeSession(session);
        }
    }

    public void updateEntity(Object obj) throws Exception{
        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            tx = session.beginTransaction();
            session.update(obj);
            session.flush();
            tx.commit();
            tx = null;
        }catch (Exception e){
            e.printStackTrace();
            DBUtil.rollBackTransaction(tx);
            throw new Exception("Error while Updating Entity");
        }finally {
            DBUtil.closeSession(session);
        }
    }

    public void deleteEntity(Object obj) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            tx = session.beginTransaction();
            session.delete(obj);
            session.flush();
            tx.commit();
            tx = null;
        }catch (Exception e){
            e.printStackTrace();
            DBUtil.rollBackTransaction(tx);
            throw new Exception("Error while deleting Entity");
        }finally {
            DBUtil.closeSession(session);
        }
    }
}
