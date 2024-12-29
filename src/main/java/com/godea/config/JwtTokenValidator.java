package com.godea.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        filterChain.doFilter(request, response);
        String jwt = request.getHeader("Authorization");

        if(jwt != null) {
            try {
                // Bearer token - токен получаемый от сервера при успешной авторизации, передаётся в виде Header строки
                jwt = jwt.substring(7);
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JwtSecret.getBytes());
                Claims claim = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
                // Получаю username
                String email = (String) claim.get("email");
                // Получаю role
                String authorities = (String) claim.get("authorities");

                List<GrantedAuthority> granted = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, granted);
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                response.setHeader(JwtConstant.JwtHeader, String.valueOf(key));
            } catch (Exception e) {
                throw new BadCredentialsException("Recieved invalid token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
