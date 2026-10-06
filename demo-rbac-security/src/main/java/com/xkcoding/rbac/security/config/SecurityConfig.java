package com.xkcoding.rbac.security.config;

import com.xkcoding.rbac.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * Security 配置
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-07 16:46
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CustomConfig.class)
public class SecurityConfig {
    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RbacAuthorityService rbacAuthorityService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(ignoredRequestMatchers()).permitAll()
                .anyRequest().access((authentication, context) -> {
                    var current = authentication.get();
                    return new AuthorizationDecision(
                        current != null && rbacAuthorityService.hasPermission(context.getRequest(), current));
                }));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private RequestMatcher[] ignoredRequestMatchers() {
        IgnoreConfig ignores = customConfig.getIgnores();
        List<RequestMatcher> matchers = new ArrayList<>();
        addMatchers(matchers, HttpMethod.GET, ignores.getGet());
        addMatchers(matchers, HttpMethod.POST, ignores.getPost());
        addMatchers(matchers, HttpMethod.DELETE, ignores.getDelete());
        addMatchers(matchers, HttpMethod.PUT, ignores.getPut());
        addMatchers(matchers, HttpMethod.HEAD, ignores.getHead());
        addMatchers(matchers, HttpMethod.PATCH, ignores.getPatch());
        addMatchers(matchers, HttpMethod.OPTIONS, ignores.getOptions());
        addMatchers(matchers, HttpMethod.TRACE, ignores.getTrace());
        ignores.getPattern().forEach(pattern -> matchers.add(PathPatternRequestMatcher.pathPattern(pattern)));
        return matchers.toArray(RequestMatcher[]::new);
    }

    private void addMatchers(List<RequestMatcher> matchers, HttpMethod method, List<String> patterns) {
        patterns.forEach(pattern -> matchers.add(PathPatternRequestMatcher.pathPattern(method, pattern)));
    }
}
