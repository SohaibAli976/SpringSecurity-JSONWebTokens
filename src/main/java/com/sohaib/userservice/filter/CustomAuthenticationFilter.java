package com.sohaib.userservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager1) {
        this.authenticationManager = authenticationManager1;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       //return super.attemptAuthentication(request, response);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Attempting to authenticate user: {}" , username);
        log.info("Attempting to authenticate password: {}" , password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authRequest);

        }
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws AuthenticationException, ServletException, IOException {
        User user=(User)authentication.getPrincipal();
        Algorithm algorithm= Algorithm.HMAC256("secret".getBytes());
        String accessToken= JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000)).
                withIssuer(request.getRequestURI().toString()).
                withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                sign(algorithm);

        String refreshToken= JWT.create().
                withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000)).
                withIssuer(request.getRequestURI().toString()).
                withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                sign(algorithm);

       // response.setHeader("access_token", accessToken);
        //response.setHeader("refresh_token", refreshToken);
        Map<String,String> map=new HashMap<>();
        map.put("access_token",accessToken);
        map.put("refresh_token",refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),map);
    log.info("User successfully authenticated: {}", authentication.getName());
        //super.successfulAuthentication(request, response, chain, authentication);
    }
}
