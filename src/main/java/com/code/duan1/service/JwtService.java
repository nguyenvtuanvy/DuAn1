package com.code.duan1.service;

import com.code.duan1.exception.RefreshTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${JWT.SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expirationAccessToken;
    @Value("${JWT.EXPIRATION_REFRESH_TOKEN}")
    private String expirationRefreshToken;

    private final UserDetailsService userDetailsService;

    private Key getSignKey(){
        byte[] keys= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }

    private Claims extractClaimsAll(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractClaimsAll(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private String generateToken(Map<String, List<String>> extractClaims, UserDetails userDetails, Long expirationToken){
        Collection<? extends GrantedAuthority> managerRoles= userDetails.getAuthorities();
        List<String> roles= managerRoles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extractClaims.put("roles", roles);
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expirationToken))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails){
        long expirationToken= Long.parseLong(expirationAccessToken);
        return generateToken(new HashMap<>(), userDetails, expirationToken );
    }
    public String generateRefreshToken(UserDetails userDetails){
        long expirationToken=  Long.parseLong(expirationRefreshToken);
        return generateToken(new HashMap<>(), userDetails,expirationToken);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public Claims decodedToken(String token){
        return extractClaimsAll(token);
    }
    private Date ExtractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
        return ExtractExpiration(token).before(new Date());
    }

    @Transactional
    public String generateTokenByRefreshToken(UserDetails userDetails) throws RefreshTokenException {
        try {
            String refreshToken = generateRefreshToken(userDetails);
            if (!isTokenExpired(refreshToken)) {
                return this.generateAccessToken(userDetails);
            } else {
                throw new RefreshTokenException("Refresh token has expired");
            }
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenException("Invalid or expired refresh token");
        }
    }
}
