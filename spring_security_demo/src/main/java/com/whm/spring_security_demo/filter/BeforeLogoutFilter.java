package com.whm.spring_security_demo.filter;

import com.whm.spring_security_demo.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author whm
 * @date 2023/7/25 9:10
 */
@Component
@RequiredArgsConstructor
public class BeforeLogoutFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains("/user/logout")) {
            String token = request.getHeader("token");
            Integer userId = jwtUtils.getElement(token, "Id", Integer.class);
            threadLocal.set(userId);
        }
        filterChain.doFilter(request, response);
    }

    public Integer getUserId() {
        Integer ret = threadLocal.get();
        threadLocal.remove();
        return ret;
    }
}
