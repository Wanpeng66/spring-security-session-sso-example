package com.wp.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wp.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: wp
 * @Title: SsoUserDetailService
 * @Description: TODO
 * @date 2020/1/9 20:59
 */
@Slf4j
@Component("customUserDetailService")
public class SsoUserDetailService implements UserDetailsService {
    @Value( "${sso.token.info:/}" )
    private String tokenInfo;
    @Autowired
    HttpUtils httpUtils;
    @Override
    public UserDetails loadUserByUsername( String token ) throws UsernameNotFoundException {
        try{
            if(StringUtils.isEmpty( token )){
                throw new UsernameNotFoundException( "token为空..." );
            }
            String[] split = token.split( "," );
            String get = httpUtils.sendHttpGet( tokenInfo + "?token=" + split[0],split[1],"" );
            JSONObject jsonObject = JSON.parseObject( get );
            User userDetails = jsonObject.toJavaObject( User.class );
            return userDetails;
        }catch(Exception e){
            log.error( "loadUserByUsername异常",e );
            throw new UsernameNotFoundException( "根据token拿不到用户信息..." );
        }

    }
}
