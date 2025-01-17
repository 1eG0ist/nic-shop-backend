package com.nic.nic_shop_task.services.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.nic_shop_task.configs.MyUserDetails;
import com.nic.nic_shop_task.services.AccessTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(MyUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roleList);
        claims.put("email", userDetails.getEmail());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSigningKey()) // Подписываем с помощью ключа
                .compact();
    }

    public String getUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> getRoles(String token) {
        Claims claims = extractAllClaims(token);
        return objectMapper.convertValue(claims.get("roles"), new TypeReference<List<String>>(){});
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Используем ключ при парсинге
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
