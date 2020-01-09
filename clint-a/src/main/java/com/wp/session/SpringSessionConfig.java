package com.wp.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author: wp
 * @Title: SpringSessionConfig
 * @Description: TODO
 * @date 2020/1/9 20:17
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "client:a")
public class SpringSessionConfig {

}
