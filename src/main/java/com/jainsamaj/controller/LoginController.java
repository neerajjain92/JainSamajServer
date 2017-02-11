package com.jainsamaj.controller;

import com.google.gson.Gson;
import com.jainsamaj.exception.AuthenticationException;
import com.jainsamaj.exception.UserCreationException;
import com.jainsamaj.model.Users;
import com.jainsamaj.model.UserProfile;
import com.jainsamaj.services.UserService;
import com.jainsamaj.util.EncryptionUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaine03 on 28/01/17.
 */

@Path("/api")
public class LoginController {

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String testGet(){
        return "Hello GET is working!";
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doLogin(String requestJSON, @CookieParam("AuthenticationKey")Cookie cookie) {

        UserProfile userProfile;
        NewCookie newCookie = null;
        Map<String,Object> loginResponse = new HashMap<>();
        Response.Status status=null;
        Users users = null;

        try {
            users = UserService.convertJSONIntoUser(requestJSON);

            try {
                UserService userService = new UserService();
                userProfile = userService.isValidUser(users);

                newCookie = new NewCookie("AuthenticationKey",new StringBuffer(EncryptionUtil.encrypt(users.getUserName())).append("$").append(users.getUserName()).toString(),"/","","Authorization Key Added ",3600,false);

                loginResponse.put("status","success");
                loginResponse.put("userProfile",userProfile);
            } catch (AuthenticationException ex) {
                loginResponse.put("error",ex.getMessage());
                loginResponse.put("status","fail");
            }
        }catch (Exception e){
            loginResponse.put("error","Request JSON is invalid");
            loginResponse.put("status","fail");
        }

        Gson gson = new Gson();
        String jsonResponse =  gson.toJson(loginResponse);
        return Response.ok(jsonResponse,MediaType.APPLICATION_JSON).cookie(newCookie).build();
    }

    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String  doSignUp(String requestJSON) {

        Users users = null;
        Map<String,Object> signUpResponse = new HashMap<>();
        Response.Status status=null;

        try {
            users = UserService.convertJSONIntoUser(requestJSON);

            try {
                UserService userService = new UserService();
                Long userId = userService.createNewUser(users);
                signUpResponse.put("userId", userId);
            } catch (UserCreationException ex) {
                signUpResponse.put("error",ex.getMessage());
                signUpResponse.put("status","fail");
            }

        }catch (Exception e){
            signUpResponse.put("error","Request JSON is invalid");
            signUpResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(signUpResponse);
    }

    @GET
    @Path("/isUserLoggedIn")
    @Produces(MediaType.APPLICATION_JSON)
    public String isUserLoggedIn() {
        Map<String, Object> loggedInResponse = new HashMap<>();
        loggedInResponse.put("status","loggedIn");
        Gson gson = new Gson();
        return gson.toJson(loggedInResponse);
    }

    @GET
    @Path("/signOut")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signOut(){
        NewCookie cookie=new NewCookie("AuthenticationKey","deleted", "/", "","Authorization Key removed from request",0,false);
        return Response.serverError().cookie(cookie).build();
    }
}
