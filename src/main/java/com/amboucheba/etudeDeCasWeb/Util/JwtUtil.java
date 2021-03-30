package com.amboucheba.etudeDeCasWeb.Util;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.CollectionShareDetails;
import com.amboucheba.etudeDeCasWeb.Models.LocationShareDetails;


import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public long extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    public long extractCollectionId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("collectionId", Long.class);
    }

    public long extractPositionId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("positionId", Long.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(AuthDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), userDetails.getUserId());
    }

    public String generateToken(CollectionShareDetails collectionShareDetails){

        return Jwts.builder()
                .claim("collectionId", collectionShareDetails.getCollectionId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return createToken(claims, subject, null);
    }

    private String createToken(Map<String, Object> claims, String subject, Long userId) {
        return Jwts.builder()
                .setClaims(claims)
                .claim("userId", userId)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateToken(LocationShareDetails locationShareDetails){

        return Jwts.builder()
                .claim("positionId", locationShareDetails.getLocationId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
