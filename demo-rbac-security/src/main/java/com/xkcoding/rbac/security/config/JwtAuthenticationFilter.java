package com.xkcoding.rbac.security.config;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.rbac.security.common.Status;
import com.xkcoding.rbac.security.exception.SecurityException;
import com.xkcoding.rbac.security.service.CustomUserDetailsService;
import com.xkcoding.rbac.security.util.JwtUtil;
import com.xkcoding.rbac.security.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Jwt 认证过滤器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-10 15:15
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomConfig customConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIgnores(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.getJwtFromRequest(request);

        if (StrUtil.isNotBlank(jwt)) {
            try {
                String username = jwtUtil.getUsernameFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (SecurityException e) {
                ResponseUtil.renderJson(response, e);
            }
        } else {
            ResponseUtil.renderJson(response, Status.UNAUTHORIZED, null);
        }

    }

    /**
     * 请求是否不需要进行权限拦截
     *
     * @param request 当前请求
     * @return true - 忽略，false - 不忽略
     */
    private boolean checkIgnores(HttpServletRequest request) {
        HttpMethod httpMethod;
        try {
            httpMethod = HttpMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException exception) {
            httpMethod = HttpMethod.GET;
        }

        if (HttpMethod.GET.equals(httpMethod) && matchesAny(customConfig.getIgnores().getGet(), httpMethod, request)) return true;
        if (HttpMethod.PUT.equals(httpMethod) && matchesAny(customConfig.getIgnores().getPut(), httpMethod, request)) return true;
        if (HttpMethod.HEAD.equals(httpMethod) && matchesAny(customConfig.getIgnores().getHead(), httpMethod, request)) return true;
        if (HttpMethod.POST.equals(httpMethod) && matchesAny(customConfig.getIgnores().getPost(), httpMethod, request)) return true;
        if (HttpMethod.PATCH.equals(httpMethod) && matchesAny(customConfig.getIgnores().getPatch(), httpMethod, request)) return true;
        if (HttpMethod.TRACE.equals(httpMethod) && matchesAny(customConfig.getIgnores().getTrace(), httpMethod, request)) return true;
        if (HttpMethod.DELETE.equals(httpMethod) && matchesAny(customConfig.getIgnores().getDelete(), httpMethod, request)) return true;
        if (HttpMethod.OPTIONS.equals(httpMethod) && matchesAny(customConfig.getIgnores().getOptions(), httpMethod, request)) return true;

        return matchesAny(customConfig.getIgnores().getPattern(), request);
    }

    private boolean matchesAny(List<String> patterns, HttpServletRequest request) {
        return patterns.stream()
            .anyMatch(pattern -> PathPatternRequestMatcher.pathPattern(pattern).matches(request));
    }

    private boolean matchesAny(List<String> patterns, HttpMethod method, HttpServletRequest request) {
        return patterns.stream()
            .anyMatch(pattern -> PathPatternRequestMatcher.pathPattern(method, pattern).matches(request));
    }

}
