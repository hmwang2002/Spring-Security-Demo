package com.whm.spring_security_demo.service.impl;

import com.whm.spring_security_demo.domain.vo.LoginUser;
import com.whm.spring_security_demo.domain.vo.Response;
import com.whm.spring_security_demo.service.LoginService;
import com.whm.spring_security_demo.utils.JwtUtils;
import com.whm.spring_security_demo.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author whm
 * @date 2023/7/22 19:31
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    @Override
    public Response login(String username, String password) {
        // AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 如果认证没通过，给出对应的提示
        if (authentication == null) {
            throw new RuntimeException("登录失败");
        }
        // 如果认证通过，使用userid生成一个jwt jwt存入Response返回
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String jwt = JwtUtils.generateToken(loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        // 把完整的用户信息存入redis  userid作为key
        redisCache.setCacheMap("login:" + loginUser.getUser().getId(), map);
        return Response.success(200, "登录成功", map);
    }
}
