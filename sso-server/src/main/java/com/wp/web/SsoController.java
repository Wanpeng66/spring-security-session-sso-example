package com.wp.web;

import com.alibaba.fastjson.JSONObject;
import com.wp.entity.Token;
import com.wp.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author: wp
 * @Title: SsoControllerlogin.html
 * @Description: TODO
 * @date 2020/1/9 17:17
 */
@Controller
@Slf4j
public class SsoController {
    @Autowired
    RedisService redisService;

    @GetMapping("/login")
    public String login( HttpServletRequest request, HttpServletResponse response ){
        String fallback = request.getParameter( "fallback" );
        String logoutUrl = request.getParameter( "logoutUrl" );
        request.getSession().setAttribute( "fallback",fallback );
        request.getSession().setAttribute( "logoutUrl",logoutUrl );
        return "login";
    }

    @GetMapping("/sso/token")
    @ResponseBody
    public String token(String tokenStr){
        if(!StringUtils.isEmpty( tokenStr )){
            Token token =  redisService.findByToken(tokenStr);
            if(null==token){
                return null;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = new User( authentication.getName(),(String)authentication.getCredentials(),authentication.getAuthorities() );
            return JSONObject.toJSONString( user );
        }

        return null;
    }


    @PostMapping("/login/Fallback")
    public String fallback(HttpServletRequest request, HttpServletResponse response){
        String fallback = (String) request.getSession().getAttribute( "fallback" );
        String logoutUrl = (String) request.getSession().getAttribute( "logoutUrl" );
        String  tokenStr = (String) request.getSession().getAttribute( "token" );
        String id = request.getSession().getId();
        if(!StringUtils.isEmpty( tokenStr )){
            Token token =  redisService.findByToken(tokenStr);
            if(!token.getClients().contains( logoutUrl )){
                token.getClients().add( logoutUrl );
            }
            return "redirect:"+fallback+"?token="+token.getUserToken()+"&session="+id;
        }

        Token token = new Token();
        token.setUserToken( UUID.randomUUID().toString() );
        token.setClients( Arrays.asList( logoutUrl ) );
        redisService.save(token);
        return "redirect:"+fallback+"?token="+token.getUserToken()+"&session="+id;

    }

    @PostMapping("/login/error")
    public void loginError(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception =
                (AuthenticationException)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if(null==exception){
            exception = (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        try {
            response.getWriter().write(exception.toString());
        }catch (IOException e) {
            log.error( "登录失败,e:",e );
        }
    }


}
