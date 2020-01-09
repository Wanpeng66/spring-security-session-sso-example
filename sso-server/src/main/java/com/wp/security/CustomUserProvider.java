package com.wp.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wp
 * @Title: CustomUserProvider
 * @Description: TODO
 * @date 2020/1/9 16:38
 */
public class CustomUserProvider implements AuthenticationProvider {
    private String fallBack = "fallBack";
    private UserDetailsService customUserDetailsService;
    public UserDetailsService getCustomUserDetailsService() {
        return customUserDetailsService;
    }
    public void setCustomUserDetailsService( UserDetailsService customUserDetailsService ) {
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {
        CustomUserToken token = (CustomUserToken)authentication;
        String  username = (String) token.getPrincipal();
        String password = (String) token.getCredentials();
        String checkCode = token.getCheckCode();

        HttpServletRequest request = ( (ServletRequestAttributes) RequestContextHolder.getRequestAttributes() ).getRequest();
        String targetCode = (String) request.getSession().getAttribute( CustomUserFilter.SPRING_SECURITY_FORM_CHECK_CODE );
        /*if(!checkCode.equals( targetCode )){
            throw new BadCredentialsException( "验证码错误..." );
        }*/
        UserDetails userDetails = this.getCustomUserDetailsService().loadUserByUsername( username );
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        CustomUserToken customUserToken = new CustomUserToken( userDetails.getUsername(),userDetails.getPassword(),"",userDetails.getAuthorities() );
        customUserToken.setDetails( token.getDetails() );
        return customUserToken;
    }

    @Override
    public boolean supports( Class<?> authentication ) {
        return CustomUserToken.class.isAssignableFrom( authentication );
    }
}
