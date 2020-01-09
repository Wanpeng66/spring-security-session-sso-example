package com.wp.security;

import com.wp.entity.CustomUserSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * @author: wp
 * @Title: SpringSecurityConfig
 * @Description: TODO
 * @date 2020/1/9 15:57
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private FindByIndexNameSessionRepository mySessionRepository;
    @Autowired
    CustomUserSecurityConfig customUserSecurityConfig;

    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(){
        return new SpringSessionBackedSessionRegistry(mySessionRepository);
    }

    @Override
    public void configure( HttpSecurity http) throws Exception {
        http.apply( customUserSecurityConfig );
        http.authorizeRequests().antMatchers( "" ).permitAll();
        http.formLogin().loginPage( "/sso/login" ).successForwardUrl( "/login/Fallback" ).failureForwardUrl( "/login/error" ).permitAll();
        http.logout().permitAll();
        http.sessionManagement().maximumSessions( 1 ).maxSessionsPreventsLogin( false )
                .sessionRegistry( springSessionBackedSessionRegistry() );
    }



}
