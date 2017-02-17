package com.jainsamaj.model;

import java.util.Date;

/**
 * Created by jaine03 on 28/01/17.
 */
public class Users {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Date creationDtm;
    private String mobileNumber;
    private String emailId;
    private String topicARN;
    private String topicSubscriptionId;
    private Date topicCreationDtm;
    private Date otpSentDtm;
    private Integer otp;
    private String pmfKey;
    private Date updationDtm;
    private Date passwordChangeDtm;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDtm() {
        return creationDtm;
    }

    public void setCreationDtm(Date creationDtm) {
        this.creationDtm = creationDtm;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTopicARN() {
        return topicARN;
    }

    public void setTopicARN(String topicARN) {
        this.topicARN = topicARN;
    }

    public String getTopicSubscriptionId() {
        return topicSubscriptionId;
    }

    public void setTopicSubscriptionId(String topicSubscriptionId) {
        this.topicSubscriptionId = topicSubscriptionId;
    }

    public Date getTopicCreationDtm() {
        return topicCreationDtm;
    }

    public void setTopicCreationDtm(Date topicCreationDtm) {
        this.topicCreationDtm = topicCreationDtm;
    }

    public Date getOtpSentDtm() {
        return otpSentDtm;
    }

    public void setOtpSentDtm(Date otpSentDtm) {
        this.otpSentDtm = otpSentDtm;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getPmfKey() {
        return pmfKey;
    }

    public void setPmfKey(String pmfKey) {
        this.pmfKey = pmfKey;
    }

    public Date getUpdationDtm() {
        return updationDtm;
    }

    public void setUpdationDtm(Date updationDtm) {
        this.updationDtm = updationDtm;
    }

    public Date getPasswordChangeDtm() {
        return passwordChangeDtm;
    }

    public void setPasswordChangeDtm(Date passwordChangeDtm) {
        this.passwordChangeDtm = passwordChangeDtm;
    }
}
