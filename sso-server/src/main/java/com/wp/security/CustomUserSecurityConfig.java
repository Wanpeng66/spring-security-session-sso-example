package com.wp.security;

import com.wp.security.CustomUserFilter;
import com.wp.security.CustomUserProvider;
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
 * @Title: CustomUserSecurityConfig
 * @Description: TODO
 * @date 2020/1/9 17:05
 */
@Component
public class CustomUserSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource(name="customUserDetailService")
    UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CustomUserFilter customUserFilter = new CustomUserFilter();
        customUserFilter.setAuthenticationManager(http.getSharedObject( AuthenticationManager.class));

        CustomUserProvider customUserProvider = new CustomUserProvider();
        customUserProvider.setCustomUserDetailsService( userDetailsService );

        http.authenticationProvider(customUserProvider)
                .addFilterAfter(customUserFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
