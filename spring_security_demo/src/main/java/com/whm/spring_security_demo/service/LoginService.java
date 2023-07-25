package com.whm.spring_security_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whm.spring_security_demo.domain.entity.UserEntity;
import com.whm.spring_security_demo.domain.vo.Response;

/**
 * @author whm
 * @date 2023/7/22 19:30
 */
public interface LoginService extends IService<UserEntity> {
    public Response login(String username, String password);

    public boolean findByUsername(String username);
}
