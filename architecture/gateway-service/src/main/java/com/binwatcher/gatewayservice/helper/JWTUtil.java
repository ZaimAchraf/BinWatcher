package com.binwatcher.gatewayservice.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${app.jwt.secret}")
    private String SECRET;

    @Value("${app.jwt.expiration}")
    private Integer DELAY;
    public Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
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
