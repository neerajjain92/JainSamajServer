package com.jainsamaj.util;

import com.jainsamaj.exception.UserCreationException;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

/**
 * Created by jaine03 on 15/02/17.
 */
public class AppUtil {

    public static String createPMFKey(final String firstName,final String lastName, final String userName) throws UserCreationException {
        StringBuffer pmfKey = new StringBuffer();

        if(lastName.length() < 3 || firstName.length() < 2){
            throw new UserCreationException("Please provide a valid first and last Name");
        }

        pmfKey.append(lastName.substring(0,3)).append(firstName.substring(0,2));

        Integer randomNumber = 0;
        while (randomNumber == 0) {
            randomNumber = new Random().nextInt(9);
        }

        pmfKey.append("0").append(randomNumber).append("-").append(userName);
        return pmfKey.toString().toLowerCase();
    }

    public static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
