package com.jainsamaj.services;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jainsamaj.dao.UserDao;
import com.jainsamaj.exception.AuthenticationException;
import com.jainsamaj.exception.UserCreationException;
import com.jainsamaj.model.ForgotPassword;
import com.jainsamaj.model.UserProfile;
import com.jainsamaj.model.Users;
import com.jainsamaj.util.AppUtil;
import com.jainsamaj.util.EncryptionUtil;
import com.jainsamaj.util.SimpleNotificationService;
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

        // Sending Login Successful Mail
        //SimpleNotificationService.publishToTopic(existingUsers.getTopicARN(),"You just logged into Jain Samaj WebPortal, if not you please report by sending email to jain007neeraj@gmail.com ");
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

                String pmfKey = null;

                try {
                    pmfKey = AppUtil.createPMFKey(users.getFirstName(), users.getLastName(), users.getUserName());
                } catch (Exception e){
                    e.printStackTrace();
                    throw new UserCreationException(e.getMessage());
                }
                users.setPmfKey(pmfKey);
                Long userId = userDao.saveEntity(users);

                logger.debug("Generated PMF Key is {} ",pmfKey);
                //Creating a Topic for Signed Up user [In order to Send SMS/EMAIl].
                if(userId != null) {
                    final String topicARN = SimpleNotificationService.createTopic(pmfKey);

                    // Topic Created Now Subscribing to The Topic
                    final String subscriptionId = SimpleNotificationService.subscribeToTopic(topicARN, users.getEmailId());

                    existingUsers = userDao.getEntityByProperty(property, Users.class);
                    existingUsers.setTopicARN(topicARN);
                    existingUsers.setTopicCreationDtm(new Date());
                    existingUsers.setTopicSubscriptionId(subscriptionId);
                    existingUsers.setUpdationDtm(new Date());
                    userDao.updateEntity(existingUsers);

                    SimpleNotificationService.publishToTopic(topicARN,"Testing Publishing To Topic with ARN "+ topicARN);
                }
                return userId;
            }catch (Exception e){
                e.printStackTrace();
                throw new UserCreationException("Error While Creating Users");
            }
        }
    }

    public static <T>T convertJSONIntoBean(String requestJSON,Class<T> type) throws Exception{
        if(!Strings.isNullOrEmpty(requestJSON)) {
            Gson gson = new Gson();
            return gson.fromJson(requestJSON, type);
        } else {
            throw new Exception();
        }
    }

    public void forgotPassword(String registeredEmailId) throws Exception {
        Map<String,Object> property = new HashMap<>();
        property.put("emailId", registeredEmailId);

        Users existingUsers = userDao.getEntityByProperty(property, Users.class);

        if(existingUsers != null) {
            String newPassword = AppUtil.generateRandomPassword();
            existingUsers.setPassword(EncryptionUtil.encrypt(newPassword));
            existingUsers.setPasswordChangeDtm(new Date());
            userDao.updateEntity(existingUsers);
            SimpleNotificationService.publishToTopic(existingUsers.getTopicARN(),"Your New Password is "+newPassword+". \n"+"Please change your password after login");
        }else {
            throw new Exception("Email Id is not registered, Please try again with Valid Id");
        }
    }

    public void changePassword(ForgotPassword forgotPasswordBean,String loggedInUser) throws Exception {
        Map<String,Object> property = new HashMap<>();
        property.put("userName", loggedInUser);

        Users existingUsers = userDao.getEntityByProperty(property, Users.class);

        if(forgotPasswordBean.getCurrentPassword() == null || forgotPasswordBean.getNewPassword() == null) {
            throw new Exception("Mandatory Parameters Missing, Please enter current and new Password");
        }else {
            if(existingUsers.getPassword().equalsIgnoreCase(EncryptionUtil.encrypt(forgotPasswordBean.getCurrentPassword()))){
                existingUsers.setPassword(EncryptionUtil.encrypt(forgotPasswordBean.getNewPassword()));
                existingUsers.setPasswordChangeDtm(new Date());
                userDao.updateEntity(existingUsers);
            }else {
                throw new Exception("Current Password is invalid");
            }
        }
    }
}
