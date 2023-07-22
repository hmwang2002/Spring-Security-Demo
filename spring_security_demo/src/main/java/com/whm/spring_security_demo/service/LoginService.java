package com.whm.spring_security_demo.service;

import com.whm.spring_security_demo.domain.vo.Response;

/**
 * @author whm
 * @date 2023/7/22 19:30
 */
public interface LoginService {
    public Response login(String username, String password);
}
