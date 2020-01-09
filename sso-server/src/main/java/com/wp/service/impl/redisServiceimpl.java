package com.wp.service.impl;

import com.wp.entity.Token;
import com.wp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: wp
 * @Title: redisServiceimpl
 * @Description: TODO
 * @date 2020/1/9 19:49
 */
@Service
public class redisServiceimpl implements RedisService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    private String prefix = "sso:token:";
    @Override
    public Token findByToken(String token) {
        Token result = (Token) redisTemplate.opsForValue().get( prefix + token );
        return result;
    }

    @Override
    public void save( Token token ) {
        redisTemplate.opsForValue().set( prefix+token.getUserToken(),token,3600, TimeUnit.SECONDS );
    }
}
