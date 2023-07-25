package com.whm.spring_security_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whm.spring_security_demo.domain.entity.UserEntity;
import com.whm.spring_security_demo.domain.vo.LoginUser;
import com.whm.spring_security_demo.domain.vo.Response;
import com.whm.spring_security_demo.mapper.UserMapper;
import com.whm.spring_security_demo.service.LoginService;
import com.whm.spring_security_demo.utils.JwtUtils;
import com.whm.spring_security_demo.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author whm
 * @date 2023/7/22 19:31
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements LoginService {
    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final JwtUtils jwtUtils;

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
        String jwt = jwtUtils.generateToken(loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        // 把完整的用户信息存入redis  userid作为key
        return Response.success(200, "登录成功", map);
    }

    @Override
    public boolean findByUsername(String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, username);
        UserEntity user = baseMapper.selectOne(queryWrapper);
        return user != null;
    }
}
