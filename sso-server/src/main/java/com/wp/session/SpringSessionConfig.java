package com.wp.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author: wp
 * @Title: SpringSessionConfig
 * @Description: TODO
 * @date 2020/1/9 19:50
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "sso:server")
public class SpringSessionConfig {
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisTemplate<String,Object> customRedisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory( redisConnectionFactory );
        template.setKeySerializer( new StringRedisSerializer(  ) );
        template.setValueSerializer( new GenericJackson2JsonRedisSerializer(  ) );
        return template;
    }
}
