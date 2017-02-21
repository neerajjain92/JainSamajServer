package com.jainsamaj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jaine03 on 29/01/17.
 */
public class EncryptionUtil {

    private static Logger logger= LoggerFactory.getLogger(EncryptionUtil.class);
    public static String encrypt(String plainText){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = plainText.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Error while encrypting Password {}",ex.getMessage());
        }
        return null;
    }
}
