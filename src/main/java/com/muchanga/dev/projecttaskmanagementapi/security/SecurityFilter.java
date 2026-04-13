package com.muchanga.dev.projecttaskmanagementapi.security;

import com.muchanga.dev.projecttaskmanagementapi.entity.user.User;
import com.muchanga.dev.projecttaskmanagementapi.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        if(token != null){
            String login = tokenService.validateToken(token);
            if(!login.isEmpty()){
                UserDetails user = userRepository.findByLogin(login)
                        .orElseThrow(()-> new RuntimeException("Usuário do token não encontrado"));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            }
        }
        filterChain.doFilter(request,response);

    }

    private String recoverToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null  || !authHeader.startsWith("Bearer ")){
            return null;
        }
        return authHeader.substring(7);
    }
}
