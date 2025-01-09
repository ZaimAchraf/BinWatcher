package com.binwatcher.securityservice.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${app.token.secret}")
    private String SECRET;

    @Value("${app.token.delay}")
    private Integer DELAY;
    public Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken (Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + DELAY * 60 * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim (String token, Function <Claims, T> resolver) {
        return resolver.apply(getAllClaims(token));
    }

    public Date getExpiration (String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public String getUsername (String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired (String token) {
        return getExpiration(token).before(new Date());
    }

    public boolean isTokenValid (String token, String username) {
        return getUsername(token).equals(username) && !isTokenExpired(token);
    }

    public Object getRoles(String token) {
        return getClaim(token, claims -> claims.get("roles"));
    }


}
