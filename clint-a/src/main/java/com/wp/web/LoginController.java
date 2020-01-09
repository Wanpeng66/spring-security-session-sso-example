package com.wp.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wp
 * @Title: LoginController
 * @Description: TODO
 * @date 2020/1/9 21:27
 */
@Controller
@Slf4j
public class LoginController  {
    @Value( "${sso.server.url:/}" )
    String ssoUrl;

    @GetMapping("/login")
    public String login(){
        return "redirect:"+ssoUrl;
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login/error")
    public void loginError( HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception =
                (AuthenticationException)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if(null==exception){
            exception = (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        try {
            response.getWriter().write(exception.toString());
        }catch (Exception e) {
            log.error( "登录失败,e:",e );
        }
    }

}
