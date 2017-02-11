package com.jainsamaj.services;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jainsamaj.dao.UserDao;
import com.jainsamaj.exception.AuthenticationException;
import com.jainsamaj.exception.UserCreationException;
import com.jainsamaj.model.UserProfile;
import com.jainsamaj.model.Users;
import com.jainsamaj.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaine03 on 28/01/17.
 */
public class UserService {

    private UserDao userDao;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(){
        this.userDao = new UserDao();
    }

    public UserProfile isValidUser(Users users) throws AuthenticationException {
        UserProfile userProfile = null;
        Map<String,Object> property = new HashMap<>();
        property.put("userName", users.getUserName());
        Users existingUsers = userDao.getEntityByProperty(property,Users.class);

        if(existingUsers != null){
            logger.info("User's Password is {} ",users.getPassword());
            if(existingUsers.getPassword().equalsIgnoreCase(EncryptionUtil.encrypt(users.getPassword()))){
                userProfile = new UserProfile();
                userProfile.setUsers(existingUsers);
                userProfile.setLastLoginDtm(new Date());
            }else {
                throw new AuthenticationException("Password is incorrect");
            }
        }else{
            throw new AuthenticationException("Users Does not exist");
        }
        return userProfile;
    }

    public Long createNewUser(Users users) throws UserCreationException {

        Map<String,Object> property = new HashMap<>();
        property.put("userName", users.getUserName());

        Users existingUsers = userDao.getEntityByProperty(property, Users.class);

        if(existingUsers !=null){
            throw new UserCreationException("UserName already registered, Please use a different one");
        }else{
            try {
                users.setPassword(EncryptionUtil.encrypt(users.getPassword()));
                users.setCreationDtm(new Date());
                return userDao.saveEntity(users);
            }catch (Exception e){
                throw new UserCreationException("Error While Creating Users");
            }
        }
    }

    public static Users convertJSONIntoUser(String requestJSON) throws Exception{
        if(!Strings.isNullOrEmpty(requestJSON)) {
            Gson gson = new Gson();
            return gson.fromJson(requestJSON, Users.class);
        } else {
            throw new Exception();
        }
    }
}
