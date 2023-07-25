package com.whm.spring_security_demo.config;

import com.whm.spring_security_demo.filter.BeforeLogoutFilter;
import com.whm.spring_security_demo.filter.JwtAuthenticationTokenFilter;
import com.whm.spring_security_demo.service.impl.UserDetailServiceImpl;
import com.whm.spring_security_demo.utils.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @author whm
 * @date 2023/7/22 19:14
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailService;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final RedisCache redisCache;
    private final BeforeLogoutFilter beforeLogoutFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain securityFilterChain = http
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement.disable())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers("/user/login", "/user/register").anonymous()
                        .anyRequest().authenticated())
                .logout(logout -> logout.logoutUrl("/user/logout").deleteCookies("JSESSIONID").logoutSuccessHandler(this::onLogOutSuccess))
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(beforeLogoutFilter, LogoutFilter.class)
                .build();
        return securityFilterChain;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * 登出成功后的处理
     */
    private void onLogOutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setHeader("token", "");
        Integer userId = beforeLogoutFilter.getUserId();
        redisCache.deleteObject("login:" + userId);
    }

    /**
     * 处理跨域
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(Duration.ofHours(1));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
