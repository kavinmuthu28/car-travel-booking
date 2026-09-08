package com.cartravel.booking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.jwt.secret:carTravelBookingSuperSecretKeyForJwtAuthentication2026ShouldBe256BitsLong!}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(jwtSecret.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenFromEmail(userPrincipal.getUsername());
    }

    public String generateTokenFromEmail(String email) {
        Date now = new Date();
        return Jwts.builder().subject(email).issuedAt(now).expiration(new Date(now.getTime() + jwtExpirationMs)).signWith(getSigningKey()).compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try { Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token); return true; }
        catch (JwtException | IllegalArgumentException e) { return false; }
    }
}