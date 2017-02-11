package com.jainsamaj.dao;

import com.jainsamaj.model.Member;
import com.jainsamaj.util.DBUtil;
import com.jainsamaj.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaine03 on 03/02/17.
 */
public class MemberDao extends GenericDao {

    public List<Member> getMembers(String createdBy) {

        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Member.class);
            criteria.add(Restrictions.eq("createdById",createdBy));
            List  list = criteria.list();
            tx.commit();
            tx = null;
            return list;
        }catch (Exception e){
            e.printStackTrace();
            DBUtil.rollBackTransaction(tx);
        }finally {
            DBUtil.closeSession(session);
        }
        return null;
    }

    public void deleteMember(String memberId, String loggedInUser) throws Exception {

        Map<String,Object> property = new HashMap<>();
        property.put("id",Long.parseLong(memberId));


        Member member = getEntityByProperty(property,Member.class);
        if(member != null){
            if(member.getCreatedById().equals(loggedInUser)){
                try {
                    deleteEntity(member);
                }catch (Exception e){
                    throw new Exception("Error while deleting Member");
                }
            }else {
                throw new Exception("You are not authorized to delete this member");
            }
        }else {
            throw new Exception("Error While Deleting Member, Member Id is invalid");
        }
    }
}
