package com.wp.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wp
 * @Title: CustomUserFilter
 * @Description: TODO
 * @date 2020/1/9 16:23
 */
public class CustomUserFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_CHECK_CODE = "checkCode";
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";


    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private String checkCodeParameter = SPRING_SECURITY_FORM_CHECK_CODE;

    private boolean postOnly = true;


    protected CustomUserFilter(  ) {
        super( new AntPathRequestMatcher("/sso", "POST") );
    }
    protected CustomUserFilter( String defaultFilterProcessesUrl ) {
        super( defaultFilterProcessesUrl );
    }

    protected CustomUserFilter( RequestMatcher requiresAuthenticationRequestMatcher ) {
        super( requiresAuthenticationRequestMatcher );
    }
    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = request.getParameter( usernameParameter );
        String password = request.getParameter( passwordParameter );
        String checkCode = request.getParameter( checkCodeParameter );



        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        CustomUserToken authRequest = new CustomUserToken(
                username, password,checkCode);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public String getCheckCodeParameter() {
        return checkCodeParameter;
    }

    public void setCheckCodeParameter( String checkCode ) {
        this.checkCodeParameter = checkCode;
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public void setUsernameParameter( String usernameParameter ) {
        this.usernameParameter = usernameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public void setPasswordParameter( String passwordParameter ) {
        this.passwordParameter = passwordParameter;
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    public void setPostOnly( boolean postOnly ) {
        this.postOnly = postOnly;
    }
}
