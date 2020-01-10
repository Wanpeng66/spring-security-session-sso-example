package com.wp.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: wp
 * @Title: SsoClientProvider
 * @Description: TODO
 * @date 2020/1/9 20:45
 */
public class SsoClientProvider implements AuthenticationProvider {

    private UserDetailsService customUserDetailsService;
    public UserDetailsService getCustomUserDetailsService() {
        return customUserDetailsService;
    }
    public void setCustomUserDetailsService( UserDetailsService customUserDetailsService ) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {
        SsoClientToken clientToken = (SsoClientToken)authentication;
        String token = clientToken.getToken();
        String session = clientToken.getSession();
        if(null==token){
            token = "";
        }
        UserDetails userDetails = this.getCustomUserDetailsService().loadUserByUsername( token+","+session );
        SsoClientToken result = new SsoClientToken( userDetails.getUsername(),userDetails.getPassword(),token,session,userDetails.getAuthorities() );
        result.setDetails( clientToken.getDetails() );
        return result;
    }

    @Override
    public boolean supports( Class<?> authentication ) {
        return SsoClientToken.class.isAssignableFrom( authentication );
    }
}
