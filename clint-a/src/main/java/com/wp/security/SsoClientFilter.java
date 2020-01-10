package com.wp.security;

import org.springframework.security.authentication.AuthenticationServiceException;
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
    public static final String SPRING_SECURITY_FORM_SESSION = "session";
    private String sessionParameter = SPRING_SECURITY_FORM_SESSION;
    private boolean postOnly = true;


    protected SsoClientFilter(  ) {
        super( new AntPathRequestMatcher("/client", "POST") );
    }

    protected SsoClientFilter( String defaultFilterProcessesUrl ) {
        super( defaultFilterProcessesUrl );
    }

    protected SsoClientFilter( RequestMatcher requiresAuthenticationRequestMatcher ) {
        super( requiresAuthenticationRequestMatcher );
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String token = request.getParameter( tokenParameter );
        String session = request.getParameter( sessionParameter );
        if(StringUtils.isEmpty( token )){
            throw new BadCredentialsException( "token为空...." );
        }
        SsoClientToken authRequest = new SsoClientToken(
                "", "",token,session);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails( HttpServletRequest request,
                               SsoClientToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly( boolean postOnly ) {
        this.postOnly = postOnly;
    }
}
