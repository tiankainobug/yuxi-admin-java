package com.yuxi.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.common.ResultCode;
import com.yuxi.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 定义需要跳过JWT校验的路径
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    );

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return EXCLUDE_PATHS.stream().anyMatch(path -> pathMatcher.match(path, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token的格式为 "Bearer token"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                // JWT解析出错时返回Result对象
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=UTF-8");
                Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED, ResultCode.UNAUTHORIZED.getMessage());
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return;
            }
        } else {
            // 没有提供Bearer Token时也返回Result对象
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED, ResultCode.UNAUTHORIZED.getMessage());
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return;
        }

        // 验证token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, username)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                // Token验证失败时返回Result对象
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=UTF-8");
                Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED, ResultCode.UNAUTHORIZED.getMessage());
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return;
            }
        } else if (username != null && jwtToken != null) {
            // 用户名存在但认证为空的情况
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED, ResultCode.UNAUTHORIZED.getMessage());
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return;
        }
        chain.doFilter(request, response);
    }
}