package com.wp.web;

import com.alibaba.fastjson.JSONObject;
import com.wp.entity.Token;
import com.wp.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/sso/login")
    public String login( HttpServletRequest request, HttpServletResponse response ){
        String fallback = request.getParameter( "fallback" );
        String logoutUrl = request.getParameter( "logoutUrl" );
        request.getSession().setAttribute( "fallback",fallback );
        request.getSession().setAttribute( "logoutUrl",logoutUrl );

        return "login";
    }

    @GetMapping("/login/fallback")
    public String fallback(HttpServletRequest request, HttpServletResponse response){
        String fallback = (String) request.getSession().getAttribute( "fallback" );
        String logoutUrl = (String) request.getSession().getAttribute( "logoutUrl" );
        String  tokenStr = (String) request.getSession().getAttribute( "token" );
        if(!StringUtils.isEmpty( tokenStr )){
            Token token =  redisService.findByToken(tokenStr);
            if(!token.getClients().contains( logoutUrl )){
                token.getClients().add( logoutUrl );
            }
            return "redirect:"+fallback+"?token="+token.getUserToken();
        }

        Token token = new Token();
        token.setUserToken( UUID.randomUUID().toString() );
        token.setClients( Arrays.asList( logoutUrl ) );
        redisService.save(token);
        return "redirect:"+fallback+"?token="+token.getUserToken();

    }


}
