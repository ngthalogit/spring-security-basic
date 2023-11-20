package com.example.springsecurity.security.filter;

import com.example.springsecurity.helper.JwtHelper;
import com.example.springsecurity.service.SpringUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtHelper jwtHelper;
    private final SpringUserService springUserService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, SpringUserService springUserService) {
        this.jwtHelper = jwtHelper;
        this.springUserService = springUserService;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getTokenFromRequest(request);
        if (StringUtils.hasText(jwt)) {
            try {
                String springUserId = jwtHelper.extractSpringUserIdFromToken(jwt);
                if (StringUtils.hasText(springUserId)) {
                    UserDetails springUser = springUserService.loadUserById(springUserId);
                    Optional.ofNullable(springUser).ifPresent((user) -> {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        securityContext.setAuthentication(authentication);
                        SecurityContextHolder.setContext(securityContext);
                    });
                }
            } catch (Exception e) {
                LOGGER.info("Failed to authenticate user");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
