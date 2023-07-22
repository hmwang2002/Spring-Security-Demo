package com.whm.spring_security_demo.controller;

import com.whm.spring_security_demo.domain.vo.Response;
import com.whm.spring_security_demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whm
 * @date 2023/7/22 19:28
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public Response login(@RequestParam String username, @RequestParam String password) {
        return loginService.login(username, password);
    }
}
