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
public class SsoClientToken extends AbstractAuthenticationToken {
    private String token ;
    private String session;
    private String username;
    private String password;

    public SsoClientToken( String username,String password,String token,String session,Collection<? extends GrantedAuthority> authorities ) {
        super( authorities );
        this.username =username;
        this.password = password;
        this.token = token;
        this.session = session;
        super.setAuthenticated( true );
    }

    public SsoClientToken( String username,String password,String token,String session ) {
        super( null );
        this.username =username;
        this.password = password;
        this.token = token;
        this.session = session;
        this.setAuthenticated( false );
    }


    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
    }

    public String getSession() {
        return session;
    }

    public void setSession( String session ) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
