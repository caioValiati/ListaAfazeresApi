package com.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import com.example.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "kZj8/XKn74XtScVhwVdKXKGeMvECwUjT9HXKMvAsWdtT+kZorUclGo3HyTDlFPBxR4ZBoTW5MvFKHgNDaL3E7g==";
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaimsFromToken(token).get("id", Long.class));
    }
}