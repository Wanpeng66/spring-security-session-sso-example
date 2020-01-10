package com.wp.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

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
        return "redirect:"+ssoUrl+"?fallback=http://localhost:9091/fallback";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/fallback")
    public void fallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter( "token" );
        String session = request.getParameter( "session" );
        response.setContentType("text/html");
        response.setCharacterEncoding( "UTF-8" );
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println(" <HEAD><TITLE>sender</TITLE></HEAD>");
        out.println(" <BODY>");
        out.println(" 请稍后... ");
        out.println("<form name=\"submitForm\" action=\"/client \" method=\"post\">");
        out.println("<input type=\"hidden\" name=\"token\" value=\"" + token + "\"/>");
        out.println("<input type=\"hidden\" name=\"session\" value=\"" + session + "\"/>");
        out.println("</from>");
        out.println("<script>window.document.submitForm.submit();</script> ");
        out.println(" </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    @RequestMapping("/login/error")
    public void loginError( HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        Exception exception =
                (Exception)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if(null==exception){
            exception = (Exception)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        try {
            response.getWriter().write(exception.getMessage().toString());
        }catch (Exception e) {
            log.error( "登录失败,e:",e );
        }
    }

}
