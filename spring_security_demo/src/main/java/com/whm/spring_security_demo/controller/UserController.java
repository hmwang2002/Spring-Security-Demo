package com.whm.spring_security_demo.controller;

import com.whm.spring_security_demo.domain.entity.UserEntity;
import com.whm.spring_security_demo.domain.vo.Response;
import com.whm.spring_security_demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whm
 * @date 2023/7/22 19:28
 */
@RestController
public class UserController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/user/login")
    public Response login(@RequestParam String username, @RequestParam String password) {
        return loginService.login(username, password);
    }

    @RequestMapping("/user/register")
    public Response register(@RequestParam String username, @RequestParam String password) {
        if (loginService.findByUsername(username)) {
            return Response.failed(999, "用户名已经存在");
        }
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        boolean res = loginService.save(user);
        if (res) {
            return Response.success(200, "注册成功");
        } else {
            return Response.failed(999, "操作失败");
        }
    }
}
