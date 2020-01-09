package com.wp.service;



import com.wp.entity.SysUser;
import com.wp.entity.SysUserRole;

import java.util.List;

/**
 * @author: wp
 * @Title: UserService
 * @Description: TODO
 * @date 2020/1/6 15:41
 */
public interface UserService {
    SysUser findById( Integer id );

    SysUser findByName( String name );

    List<SysUserRole> listByUserId( Integer id );
}
