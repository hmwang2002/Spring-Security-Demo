package com.whm.spring_security_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whm.spring_security_demo.domain.entity.UserEntity;
import com.whm.spring_security_demo.domain.vo.LoginUser;
import com.whm.spring_security_demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author whm
 * @date 2023/7/22 18:29
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, username);
        UserEntity user = userMapper.selectOne(queryWrapper);
        // 如果没有查询到用户就抛出异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        user.setPassword("{bcrypt}" + user.getPassword());
        //TODO：查询对应的权限信息,一般实现思路是从数据库中查询权限信息，不过我并没有将权限信息读取到redis中，如果有权限要求的话
        // 后续还要修改redis的读取

        return new LoginUser(user);
    }
}
