package com.jainsamaj.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by jaine03 on 29/01/17.
 */
public class DBUtil {

    public static void  rollBackTransaction(Transaction transaction){
        if(transaction!=null){
            transaction.rollback();
        }
    }

    public static void closeSession(Session session){
        if(session != null){
            session.close();
        }
    }
}
