package com.example.librarymanagement.app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.librarymanagement.app.config.UserInfoUserDetailsService;
import com.example.librarymanagement.app.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {
        private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper; 

   @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

        logger.info("Request Method: {} | Request URL: {}", request.getMethod(), request.getRequestURI());

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleError(response, HttpStatus.UNAUTHORIZED, "JWT Token has expired", request.getRequestURI());
        } catch (SignatureException ex) {
            handleError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT signature", request.getRequestURI());
        } catch (Exception ex) {
            handleError(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
        }
    }

     private void handleError(HttpServletResponse response, HttpStatus status, String message, String path) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        errorDetails.put("path", path);
        
        logger.error("Exception: {}", message, status);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorDetails));
    }
}
    