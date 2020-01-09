package com.wp.service;

import com.alibaba.fastjson.JSONObject;
import com.wp.entity.Token;

/**
 * @author: wp
 * @Title: RedisService
 * @Description: TODO
 * @date 2020/1/9 17:27
 */
public interface RedisService {

    Token findByToken(String token);

    void save( Token token );
}
