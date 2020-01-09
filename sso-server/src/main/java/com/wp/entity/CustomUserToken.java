package com.wp.entity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: wp
 * @Title: CustomUserToken
 * @Description: TODO
 * @date 2020/1/9 16:20
 */
public class CustomUserToken extends UsernamePasswordAuthenticationToken {
    private String checkCode;

    public CustomUserToken( Object principal, Object credentials,String checkCode ) {
        super( principal, credentials );
        this.checkCode = checkCode;
    }

    public CustomUserToken( Object principal, Object credentials,String checkCode,  Collection<? extends GrantedAuthority> authorities ) {
        super( principal, credentials, authorities );
        this.checkCode = checkCode;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode( String checkCode ) {
        this.checkCode = checkCode;
    }


}
