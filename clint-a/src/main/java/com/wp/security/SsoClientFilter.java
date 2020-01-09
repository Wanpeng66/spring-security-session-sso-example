package com.wp.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wp
 * @Title: SsoClientFilter
 * @Description: TODO
 * @date 2020/1/9 20:37
 */
public class SsoClientFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_TOKEN = "token";
    private String tokenParameter = SPRING_SECURITY_FORM_TOKEN;
    private boolean postOnly = false;


    protected SsoClientFilter(  ) {
        super( new AntPathRequestMatcher("/clienta/fallback", "GET") );
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException, IOException, ServletException {
        String token = request.getParameter( tokenParameter );
        if(StringUtils.isEmpty( token )){
            throw new BadCredentialsException( "token为空...." );
        }
        SsoClientToken authRequest = new SsoClientToken(
                "", "",token);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly( boolean postOnly ) {
        this.postOnly = postOnly;
    }
}
