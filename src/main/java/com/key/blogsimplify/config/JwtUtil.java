package com.key.blogsimplify.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * JWT 工具类，负责生成与校验 Token。
 * <p>
 * 示例中使用固定密钥方便演示，生产环境应存放在配置文件或环境变量中，并保证密钥长度足够安全。
 */
public class JwtUtil {

    /** 签名密钥，需要满足 HMAC-SHA 算法的长度要求。 */
    private static final String SECRET_KEY = "KEY航行日记的超级秘密密钥234567890KEY";

    /** Token 有效期：1 小时（单位：毫秒）。 */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private static final String ROLE_CLAIM = "role";

    /** 将字符串密钥转换成 {@link SecretKey} 供 JJWT 使用。 */
    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据用户名生成 JWT。
     *
     * @param username 需要写入 Token 的用户名
     * @return 已签名的 Token 字符串
     */
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim(ROLE_CLAIM, role)
                .signWith(getKey())
                .compact();
    }

    /**
     * 校验 Token 并返回用户名。
     *
     * @param token 前端携带的 Token
     * @return 合法返回用户名，非法或过期返回 null
     */
    public static Optional<Claims> parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 从 Authorization 请求头中提取真实 token，兼容 "Bearer " 前缀。
     */
    public static String resolveToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (authorizationHeader.toLowerCase(Locale.ROOT).startsWith("bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }

    public static long getExpirationSeconds() {
        return EXPIRATION_TIME / 1000;
    }

    public static String extractRole(Claims claims) {
        return claims.get(ROLE_CLAIM, String.class);
    }
}
