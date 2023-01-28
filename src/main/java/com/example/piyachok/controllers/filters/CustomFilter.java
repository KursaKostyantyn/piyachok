package com.example.piyachok.controllers.filters;

import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.User;
import com.example.piyachok.security.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private UserDAO userDAO;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.replace("Bearer ", "");
            try {
                String subject = Jwts.parser()
                        .setSigningKey("secretKey".getBytes())
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                User userByLogin = userDAO.findUserByLogin(subject).orElse(new User());
                if (userByLogin.getLogin() != null) {
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            userByLogin.getLogin(),
                                            userByLogin.getPassword(),
                                            Collections.singletonList(new SimpleGrantedAuthority(userByLogin.getRole().name()))
                                    )
                            );
                }
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "expired");
                return;
            }
        }
        filterChain
                .doFilter(request, response);

    }
}
