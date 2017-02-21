package com.jainsamaj.filter;

import com.google.common.base.Strings;
import com.jainsamaj.util.EncryptionUtil;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Created by jaine03 on 04/02/17.
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String AUTHENTICATION_HEADER="AuthenticationKey";


    @Override
    public ContainerRequest filter(ContainerRequest request) {
        Map<String,Cookie> cookies=request.getCookies();
        String requestURI=request.getRequestUri().toString();
        String []authKey=null;
        Cookie cookie=cookies.get(AUTHENTICATION_HEADER);
        logger.info("Request URI is {} ",requestURI);
        if(!requestURI.contains("login") && !requestURI.contains("signup") && !requestURI.contains("forgotPassword") && !requestURI.contains("getDashboardData")){
            if(cookie!=null && !Strings.isNullOrEmpty(cookie.getValue())){
                logger.info("Authentication Key : {}",cookie.getValue());
                authKey=cookie.getValue().split("\\$");
                if(authKey.length>=2){
                    if(!authKey[0].equals(EncryptionUtil.encrypt(authKey[1]))){
                        logger.info("Authentication Failed");
                        throw new WebApplicationException(Response.Status.FORBIDDEN);
                    }else{
                        logger.info("Authentication Successful at Server End");
                    }
                }
            }else{
                logger.info("Cookies Not Available in Request");
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
        return request;
    }
}
