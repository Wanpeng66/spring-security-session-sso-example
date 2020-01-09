package com.wp.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author: wp
 * @Title: SsoClientToken
 * @Description: TODO
 * @date 2020/1/9 20:29
 */
public class SsoClientToken extends UsernamePasswordAuthenticationToken {
    private String token ;

    public SsoClientToken( Object principal, Object credentials,String token ) {
        super( principal, credentials );
        this.token = token;
    }

    public SsoClientToken( Object principal, Object credentials,String token, Collection<? extends GrantedAuthority> authorities ) {
        super( principal, credentials, authorities );
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
    }
}
