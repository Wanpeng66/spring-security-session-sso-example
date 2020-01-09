package com.wp.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wp
 * @Title: Token
 * @Description: TODO
 * @date 2020/1/9 17:29
 */
@Data
public class Token implements Serializable {
    private String userToken;
    private List<String> clients;

}
