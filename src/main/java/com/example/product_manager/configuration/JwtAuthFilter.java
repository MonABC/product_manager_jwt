package com.example.product_manager.configuration;

import com.example.product_manager.model.entity.Role;
import com.example.product_manager.model.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtAuthFilter extends OncePerRequestFilter {
    protected JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try { //fix the CORS issue on backend side
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "20000");
            response.setHeader("Access-Control-Allow-Headers",
                    "AccessToken, JCookieId, authorization, content-type, xsrf-token, FingerPrint, x-auth-token, x-requested-with, accept ,Origin, Access-Control-Allow-Headers, Access-Control-Allow-Origin");
            response.addHeader("Access-Control-Expose-Headers", "xsrf-token, Content-Disposition");

            if ("OPTIONS".equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            String token = getJwtFromRequest(request); //lấy chuỗi jwt từ request gửi lên
            if (token != null && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUserNameFromJwtToken(token); //lấy ra username đăng nhập;
                User userDetails = jwtProvider.getUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, getAuthorities(new HashSet<>(userDetails.getRoles())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // set lại thông tin cho security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Can not set user  authentication -> Message: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            return authHeader.replace("Bearer", "");
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            roles.stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }
        return authorities;
    }
}
