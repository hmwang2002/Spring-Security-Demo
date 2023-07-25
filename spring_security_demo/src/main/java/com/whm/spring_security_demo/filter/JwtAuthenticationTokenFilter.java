package com.whm.spring_security_demo.filter;

import com.whm.spring_security_demo.utils.JwtUtils;
import com.whm.spring_security_demo.utils.RedisCache;
import com.whm.spring_security_demo.utils.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author whm
 * @date 2023/7/22 22:46
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String username;
        try {
            username = jwtUtils.getElement(token, "Username", String.class);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtils.setFailedResponse(response, 403, "token非法");
            return;
        }
        // token过期的问题
        if (!jwtUtils.validateToken(token)) {
            // redis中找不到对应token, 说明登录过期
            ResponseUtils.setFailedResponse(response, 403, "登录过期");
            return;
        }
        // 从redis中获取用户信息
        String redisKey = username;
        String redisToken = redisCache.getCacheObject(redisKey);
        if (!redisToken.equals(token)) {
            // redis中存储的token和request中的token不同
            ResponseUtils.setFailedResponse(response, 403, "用户未登录");
            return;
        }
        // 存入SecurityContextHolder
        // 获取权限信息封装到Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        response.setHeader("token", token);
        // 放行
        filterChain.doFilter(request, response);
    }
}
