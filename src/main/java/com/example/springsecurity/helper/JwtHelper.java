package com.example.springsecurity.helper;

import com.example.springsecurity.model.SpringUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtHelper.class);
    private final String JWT_SECRET = "secret";

    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(SpringUserDetails springUserDetails) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(springUserDetails.getSpringUser().getId())
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

    }

    public String extractSpringUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (MalformedJwtException e) {
            LOGGER.info("Invalid JWT");
        } catch (ExpiredJwtException e) {
            LOGGER.info("Expired JWT");
        } catch (UnsupportedJwtException e) {
            LOGGER.info("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            LOGGER.info("JWT claims string is empty.");
        }
        return null;
    }
}
