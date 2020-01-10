package com.wp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
       /* http.apply( customUserSecurityConfig ).and()*/
                http.authorizeRequests().antMatchers( "/login","/favicon.ico" ).permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage( "/login" ).loginProcessingUrl( "/sso" )
                .successForwardUrl( "/login/Fallback" )
                .failureForwardUrl( "/login/error" ).permitAll()
                .and().sessionManagement().maximumSessions( 1 ).maxSessionsPreventsLogin( false )
                .sessionRegistry( springSessionBackedSessionRegistry() );
        http.csrf().disable();
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth) throws Exception{
        //基于内存来存储用户信息
        /*auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("123")).roles("USER").and()
                .withUser("admin").password(new BCryptPasswordEncoder().encode("456")).roles("USER","ADMIN");*/
        auth.userDetailsService(customUserSecurityConfig.userDetailsService ).passwordEncoder( new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        } );

    }



}
