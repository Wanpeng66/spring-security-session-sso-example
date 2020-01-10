package com.wp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    SsoClientConfig ssoClientConfig;
    @Qualifier("customUserDetailService")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private FindByIndexNameSessionRepository mySessionRepository;


    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(){
        return new SpringSessionBackedSessionRegistry(mySessionRepository);
    }

    @Override
    public void configure( HttpSecurity http) throws Exception {
        http.apply( ssoClientConfig )
                .and().authorizeRequests().antMatchers( "/fallback","/favicon.ico","/login/error" ).permitAll()
                .anyRequest().authenticated()
        .and().formLogin().loginPage( "/login" ).loginProcessingUrl( "/client" )
                .successForwardUrl( "/index" ).failureForwardUrl( "/login/error" ).permitAll()
        .and().sessionManagement().maximumSessions( 1 ).maxSessionsPreventsLogin( false )
                .sessionRegistry( springSessionBackedSessionRegistry() );
        http.csrf().disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //此参数为true时，会对UsernameNotFoundException进行包装，变成BadCredentialsException
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        } );
        return provider;
    }
}
