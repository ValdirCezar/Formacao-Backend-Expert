package br.com.valdircezar.authserviceapi.utils;

import br.com.valdircezar.authserviceapi.security.dtos.UserDetailsDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

        public String generateToken(final UserDetailsDTO detailsDTO) {
            return Jwts.builder()
                    .claim("id", detailsDTO.getId())
                    .claim("name", detailsDTO.getName())
                    .claim("authorities", detailsDTO.getAuthorities())
                    .setSubject(detailsDTO.getUsername())
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .compact();
        }
}
