package com.jainsamaj.controller;

import com.google.gson.Gson;
import com.jainsamaj.dto.DashboardDTO;
import com.jainsamaj.exception.AuthenticationException;
import com.jainsamaj.exception.UserCreationException;
import com.jainsamaj.model.ForgotPassword;
import com.jainsamaj.model.UserProfile;
import com.jainsamaj.model.Users;
import com.jainsamaj.services.LoginService;
import com.jainsamaj.services.UserService;
import com.jainsamaj.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger= LoggerFactory.getLogger(LoginController.class);

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String testGet(){
        return "Hello GET is working!";
    }

    @GET
    @Path("/getDashboardData")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDashboardData() {
        Map<String,Object> dashboardResponse = new HashMap<>();

        try {
            LoginService service = new LoginService();
            DashboardDTO dashboardDTO = service.getDashboardData();
            dashboardResponse.put("status","success");
            dashboardResponse.put("_result",dashboardDTO);
        }catch (Exception ex){
            dashboardResponse.put("status","fail");
            dashboardResponse.put("error",ex.getMessage());
        }
        Gson gson = new Gson();
        return gson.toJson(dashboardResponse);
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
            users = UserService.convertJSONIntoBean(requestJSON,Users.class);

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
            users = UserService.convertJSONIntoBean(requestJSON,Users.class);

            try {
                UserService userService = new UserService();
                Long userId = userService.createNewUser(users);
                signUpResponse.put("userId", userId);
                signUpResponse.put("note","User Created Successfully, Please Check your email and confirm subscription.");
            } catch (UserCreationException ex) {
                signUpResponse.put("error",ex.getMessage());
                signUpResponse.put("status","fail");
            }

        }catch (Exception e){
            e.printStackTrace();
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

    @POST
    @Path("/forgotPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String forgotPassword(String requestJSON) {
        Map<String,Object> forgotPasswordResponse = new HashMap<>();
        Response.Status status=null;

        try {
            Users users = UserService.convertJSONIntoBean(requestJSON,Users.class);
            UserService userService = new UserService();
            try {
                userService.forgotPassword(users.getEmailId());
                forgotPasswordResponse.put("status","success");
            }catch (Exception ex){
                logger.error("Error in processing forgotPassword Request {}",ex.getMessage());
                forgotPasswordResponse.put("status","fail");
                forgotPasswordResponse.put("error",ex.getMessage());
            }
        }catch (Exception e){

            forgotPasswordResponse.put("error","Request JSON is invalid");
            forgotPasswordResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(forgotPasswordResponse);
    }

    @POST
    @Path("/changePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String changePassword(String requestJSON,@CookieParam("AuthenticationKey")Cookie cookie) {
        Map<String,Object> changePasswordResponse = new HashMap<>();
        Response.Status status=null;
        String authKey = cookie.getValue();
        String loggedInUser = authKey.split("\\$")[1];
        try {
            UserService userService = new UserService();
            ForgotPassword forgotPasswordBean = userService.convertJSONIntoBean(requestJSON,ForgotPassword.class);

            try {
                userService.changePassword(forgotPasswordBean, loggedInUser);
                changePasswordResponse.put("status","success");
                changePasswordResponse.put("message","Password Changed Successfully, Please refresh the page.");
            }catch (Exception ex){
                changePasswordResponse.put("status","fail");
                changePasswordResponse.put("error",ex.getMessage());
            }
        }catch (Exception ex){
            logger.error("Error in processing changePassword Request {}",ex.getMessage());
            changePasswordResponse.put("error","Request JSON is invalid");
            changePasswordResponse.put("status","fail");
        }
        Gson gson = new Gson();
        return gson.toJson(changePasswordResponse);
    }
}
