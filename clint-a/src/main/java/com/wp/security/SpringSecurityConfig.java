package com.wp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * @author: wp
 * @Title: SpringSecurityConfig
 * @Description: TODO
 * @date 2020/1/9 20:18
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Value( "${sso.server.url:/}" )
    String ssoUrl;
    @Autowired
    SsoClientConfig ssoClientConfig;

    @Autowired
    private FindByIndexNameSessionRepository mySessionRepository;


    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(){
        return new SpringSessionBackedSessionRegistry(mySessionRepository);
    }

    @Override
    public void configure( HttpSecurity http) throws Exception {
        http.apply( ssoClientConfig );
        http.authorizeRequests().antMatchers( "/" ).permitAll();
        http.formLogin().loginPage( ssoUrl ).loginProcessingUrl( "/clienta/fallback" )
                .successForwardUrl( "/index" ).failureForwardUrl( "/login/error" ).permitAll();
        http.logout().permitAll();
        http.sessionManagement().maximumSessions( 1 ).maxSessionsPreventsLogin( false )
                .sessionRegistry( springSessionBackedSessionRegistry() );
    }
}
