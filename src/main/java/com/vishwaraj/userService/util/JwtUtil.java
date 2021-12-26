package com.vishwaraj.userService.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishwaraj.userService.domain.Users;
import com.vishwaraj.userService.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class JwtUtil {
    private String SECRET_KEY = "secret";

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> cliamsResolver){
        final Claims claims = extractAllClaim(token);
        return cliamsResolver.apply(claims);
    }

    public Claims extractAllClaim(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Optional<?> generateToken(UserDetails user){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user);
    }

    private Optional<?> createToken(Map<String,Object> claims, UserDetails user){


        Date expiresAccessTokenAt = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        Date expiresRefreshTokenAt = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAccessTokenAt)
                .withIssuer(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresRefreshTokenAt)
                .withIssuer(user.getUsername())
                .sign(algorithm);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
        token.put("refresh_token", refresh_token);
        token.put("validity", expiresRefreshTokenAt.toString());

        return Optional.of(token);
    }

    public Boolean validateToken(String token, Users users){
        final String userName = extractUserName(token);
        return (userName.equals(users.getUserName()) && !isTokenExpired(token));
    }

}
