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
        String  token = (String) request.getSession().getAttribute( "token" );
        //如果之前没登陆过
        if(StringUtils.isEmpty( token )){
            return "login";
        }else{
            return "forward:/login/fallback";
        }

    }

    @GetMapping("/login/fallback")
    public void fallback(HttpServletRequest request, HttpServletResponse response){
        String fallback = request.getParameter( "fallback" );
        String logoutUrl = request.getParameter( "logoutUrl" );
        String  tokenStr = (String) request.getSession().getAttribute( "token" );
        Token token =  redisService.findByToken();
        if(!token.getClients().contains( logoutUrl )){
            token.getClients().add( logoutUrl );
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    }


}
