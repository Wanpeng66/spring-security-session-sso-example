package com.wp.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: wp
 * @Title: SsoClientConfig
 * @Description: TODO
 * @date 2020/1/9 20:55
 */
@Component
public class SsoClientConfig  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource(name="customUserDetailService")
    UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SsoClientFilter customUserFilter = new SsoClientFilter();
        customUserFilter.setAuthenticationManager(http.getSharedObject( AuthenticationManager.class));

        SsoClientProvider customUserProvider = new SsoClientProvider();
        customUserProvider.setCustomUserDetailsService( userDetailsService );

        http.authenticationProvider(customUserProvider)
                .addFilterAfter(customUserFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
