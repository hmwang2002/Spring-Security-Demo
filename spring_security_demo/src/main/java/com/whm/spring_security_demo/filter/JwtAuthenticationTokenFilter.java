package com.whm.spring_security_demo.filter;


import com.whm.spring_security_demo.domain.vo.LoginUser;
import com.whm.spring_security_demo.utils.JwtUtils;
import com.whm.spring_security_demo.utils.RedisCache;
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
import java.util.Map;

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
        String userid;
        try {
            userid = Integer.toString(jwtUtils.getElement(token, "Id", Integer.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 从redis中获取用户信息
        String redisKey = "login:" + userid;
        Map<String, String> tokenMap = redisCache.getCacheMap(redisKey);
        if (tokenMap == null) {
            throw new RuntimeException("用户不存在");
        }
        String redisToken = tokenMap.get("token");
        if (!redisToken.equals(token)) {
            throw new RuntimeException("用户未登录");
        }
        // TODO:token过期的问题
        if (!jwtUtils.validateToken(token)) {

        }
        String username = jwtUtils.getElement(token, "Username", String.class);
        // 存入SecurityContextHolder
        // TODO 获取权限信息封装到Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        response.setHeader("token", token);
        // 放行
        filterChain.doFilter(request, response);
    }
}
