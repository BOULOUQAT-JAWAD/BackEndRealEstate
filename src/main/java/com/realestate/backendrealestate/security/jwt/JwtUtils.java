package com.realestate.backendrealestate.security.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {

    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    private JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final Environment environment;


    public JwtUtils(Environment environment) {
        this.environment = environment;
        this.secretKey = this.environment.getProperty("jwt.secretKey");
        this.jwtParser = Jwts.parser().setSigningKey(this.secretKey);

    }


    public String generateToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities().toString());
        Date tokenCreationTime = new Date();
        Date tokenValidity = new Date(tokenCreationTime.getTime() + expirationTime);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null) {
            return parseClaims(token);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }


    public boolean validateClaims(Claims claims, UserDetails userDetails) {
        return (claims.getExpiration().after(new Date()) && (getEmail(claims).equals(userDetails.getUsername())));
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    public boolean validateToken(String accessToken, UserDetails userDetails) {
        Claims claims = parseClaims(accessToken);
        return validateClaims(claims, userDetails);
    }
}
