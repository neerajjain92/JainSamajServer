package com.jainsamaj.services;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jainsamaj.dao.MemberDao;
import com.jainsamaj.exception.AddMemberException;
import com.jainsamaj.model.Member;

import java.util.Date;
import java.util.List;

/**
 * Created by jaine03 on 03/02/17.
 */
public class MemberService {

    private MemberDao memberDao;

    public MemberService(){
        this.memberDao = new MemberDao();
    }

    public static Member convertJSONIntoMember(String requestJSON) throws Exception{
        if(!Strings.isNullOrEmpty(requestJSON)) {
            Gson gson = new Gson();
            return gson.fromJson(requestJSON, Member.class);
        } else {
            throw new Exception();
        }
    }

    public Long addMember(Member member,String loggedInUser) throws AddMemberException {
        Long memberId = null;
        try {
            member.setCreationDtm(new Date());
            member.setCreatedById(loggedInUser);
            return memberDao.saveEntity(member);
        }catch (Exception e){
            throw new AddMemberException(e.getMessage());
        }
    }

    public List<Member> getMembers(String createdBy) {
        return memberDao.getMembers(createdBy);
    }

    public void deleteMember(String memberId, String loggedInUser) throws Exception {
        new MemberDao().deleteMember(memberId, loggedInUser);
    }
}
