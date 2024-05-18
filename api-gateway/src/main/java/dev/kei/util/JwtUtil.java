package dev.kei.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    public static final String SECRET = "8e4da306bde7be8009e1bbb03325090f32de6d0d8847a28b7ae5db10138225079d734b252cdbd23b71aa6ab62d88de5ce8eda8667eab1e7a751b4549f57f8973";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
