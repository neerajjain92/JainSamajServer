package com.jainsamaj.model;

import java.util.Date;

/**
 * Created by jaine03 on 28/01/17.
 */
public class UserProfile {

    public Users users;
    public Date lastLoginDtm;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Date getLastLoginDtm() {
        return lastLoginDtm;
    }

    public void setLastLoginDtm(Date lastLoginDtm) {
        this.lastLoginDtm = lastLoginDtm;
    }
}
