package com.jainsamaj.controller;

import com.google.gson.Gson;
import com.jainsamaj.exception.AddMemberException;
import com.jainsamaj.model.Member;
import com.jainsamaj.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaine03 on 03/02/17.
 */
@Path("/member")
public class MemberController {

    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Path("/addMember")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addMember(@CookieParam("AuthenticationKey")Cookie cookie, String requestJSON){

        if(cookie!=null){
            logger.info("UserDetails Cookies with Client Request {}",cookie.getValue());
        }
        String authKey = cookie.getValue();
        String loggedInUser = authKey.split("\\$")[1];

        Map<String,Object> addMemberResponse = new HashMap<>();
        Response.Status status=null;
        Member member = null;

        try {
            member = MemberService.convertJSONIntoMember(requestJSON);

            try {
                MemberService memberService = new MemberService();
                Long memberId = memberService.addMember(member,loggedInUser);
                addMemberResponse.put("memberId",memberId);
            }catch (AddMemberException ex) {
                addMemberResponse.put("error",ex.getMessage());
                addMemberResponse.put("status","fail");
            }

        }catch (Exception e){
            e.printStackTrace();
            addMemberResponse.put("error","Request JSON is invalid");
            addMemberResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(addMemberResponse);
    }

    @Path("/getMembers/{createdBy}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMembers(@CookieParam("AuthenticationKey")Cookie cookie,@PathParam("createdBy") String createdBy){
        if(cookie!=null){
            logger.info("UserDetails Cookies with Client Request {}",cookie.getValue());
        }
        Map<String,Object> getMembersResponse = new HashMap<>();
        Response.Status status=null;
        List<Member> memberList = null;

        try {
            MemberService memberService = new MemberService();
            memberList = memberService.getMembers(createdBy);
            getMembersResponse.put("members",memberList);
        }catch (AddMemberException ex){
            getMembersResponse.put("error",ex.getMessage());
            getMembersResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(getMembersResponse);
    }

    @Path("/deleteMember/{memberId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteMember(@CookieParam("AuthenticationKey")Cookie cookie,@PathParam("memberId")String memberId){

        String authKey = cookie.getValue();
        String loggedInUser = authKey.split("\\$")[1];

        Map<String,Object> deleteMemberResponse = new HashMap<>();
        Response.Status status=null;
        Member member = null;

        try {
            MemberService memberService = new MemberService();
            memberService.deleteMember(memberId,loggedInUser);
            deleteMemberResponse.put("message","Member Deleted Successfully");
        }catch (Exception ex){
            deleteMemberResponse.put("error",ex.getMessage());
            deleteMemberResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(deleteMemberResponse);
    }
}
