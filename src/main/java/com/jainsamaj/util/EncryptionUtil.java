package com.jainsamaj.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jaine03 on 29/01/17.
 */
public class EncryptionUtil {

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
            ex.printStackTrace();
        }
        return null;
    }
}
