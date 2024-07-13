package com.app.shwe.securityConfig;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "AL133Dx08UYJwL7YalPHs5uqq6hFoNX4CyGUrhNzTDN+/GehW+pZQ0sGH+Y76+Ditt4xzwcn5HTuLCIuMbyX6/7hpcyz+d1Y4pt+gpQcCkvay6gppjDOojrsehZSz/0PwR2BnaY4aK06QDoTnjwpSC6DPwQjVUf5nKYZByVK67HPPu3hxMKm+GkxUwQpswy8TxV1iLWKkSHAyNNySNxnOYF/RJq+7JwKMnaGBOxrS6OnBIRx3nzghyY+ZJ/6lCRKHgsxhuzDQvhC8UDWMK5tAjI5OIOAAtJAux9XORyFibH6bZSjMFI2hhFgGxwzAoGh1S0R+nOR3Ak5HsNALzHvjJWD95HPh1jf2vYJdpeAj78";

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails.getUsername());
    }
    
    public String generateToken(String phoneNumber) {
        return generateToken(new HashMap<>(), phoneNumber);
    }


    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
