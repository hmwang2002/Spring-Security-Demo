package com.whm.spring_security_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whm
 * @date 2023/7/22 16:33
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
