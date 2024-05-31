package br.com.valdircezar.helpdeskbff.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public Claims getClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject() != null ? claims.getSubject() : null;
    }

    public List<GrantedAuthority> getAuthorities(Claims claims) {
        if(claims.get("authorities") != null) {
            var authorities = (List<LinkedHashMap<String, String>>) claims.get("authorities");

            return authorities.stream()
                    .map(authority -> (GrantedAuthority) () -> authority.get("authority"))
                    .toList();
        }
        throw new RuntimeException("Invalid token");
    }
}
