/*
 * Copyright (c) 2024 Richard Zhang (richard.jih.zhang@gmail.com).
 * Website: https://zhang-jihao.com
 * All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.family.diary.common.utils.web.jwt;

import com.family.diary.common.constants.common.JWTConstants;
import com.family.diary.common.constants.redis.RedisConstants;
import com.family.diary.common.enums.jwt.TokenType;
import com.family.diary.common.utils.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * JWT Token处理工具类
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUtil {

    private final RedisUtil redisUtil;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.token-redis-prefix}")
    private String JWT_REDIS_KEY_PREFIX;

    // ==================== 公开API（门面方法） ====================

    /**
     * 生成Access Token
     *
     * @param openId 微信OpenID
     * @return Access Token String
     */
    public String generateAccessToken(String openId) {
        return generateToken(openId, TokenType.ACCESS);
    }

    /**
     * 生成Refresh Token
     *
     * @param openId 微信OpenID
     * @return Refresh Token String
     */
    public String generateRefreshToken(String openId) {
        return generateToken(openId, TokenType.REFRESH);
    }

    /**
     * 验证Access Token是否有效
     *
     * @param token  Access Token
     * @param openId 微信OpenID
     * @return 是：有效 / 否：无效
     */
    public Boolean validateAccessToken(String token, String openId) {
        return validateToken(token, openId, TokenType.ACCESS);
    }

    /**
     * 验证Refresh Token是否有效
     *
     * @param token  Refresh Token
     * @param openId 微信OpenID
     * @return 是：有效 / 否：无效
     */
    public Boolean validateRefreshToken(String token, String openId) {
        return validateToken(token, openId, TokenType.REFRESH);
    }

    /**
     * 删除用户的所有Token（用于登出）
     *
     * @param openId 微信OpenID
     */
    public void invalidateAllTokens(String openId) {
        for (TokenType tokenType : TokenType.values()) {
            redisUtil.delete(buildTokenRedisKey(openId, tokenType));
        }
    }

    // ==================== Token信息提取 ====================

    /**
     * 从Token中提取微信OpenID
     *
     * @param token token
     * @return 微信OpenID
     */
    public String extractOpenId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 提取Token类型
     *
     * @param token token
     * @return Token类型
     */
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(JWTConstants.CLAIM_TOKEN_TYPE, String.class));
    }

    /**
     * 提取特定声明
     *
     * @param token          token
     * @param claimsResolver claimsResolver
     * @param <T>            T
     * @return 特定声明
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ==================== 核心私有方法（参数化实现） ====================

    /**
     * 生成Token（核心方法）
     *
     * @param openId    微信OpenID
     * @param tokenType Token类型
     * @return Token String
     */
    private String generateToken(String openId, TokenType tokenType) {
        var token = Jwts.builder()
                .setSubject(openId)
                .claim(JWTConstants.CLAIM_TOKEN_TYPE, tokenType.getType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenType.getExpiration()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();

        var redisKey = buildTokenRedisKey(openId, tokenType);
        redisUtil.delete(redisKey);
        redisUtil.setWithExpire(redisKey, token, tokenType.getExpiration(), TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 验证Token（核心方法）
     *
     * @param token     Token
     * @param openId    微信OpenID
     * @param tokenType 期望的Token类型
     * @return 是：有效 / 否：无效
     */
    private Boolean validateToken(String token, String openId, TokenType tokenType) {
        try {
            final var extractedOpenId = extractOpenId(token);
            final var extractedType = extractTokenType(token);

            // 验证Token类型
            if (!tokenType.getType().equals(extractedType)) {
                log.warn("Token类型不匹配，期望: {}, 实际: {}", tokenType.getType(), extractedType);
                return false;
            }

            // 验证Redis中存储的Token
            var storedToken = redisUtil.get(buildTokenRedisKey(openId, tokenType));
            var isValidToken = storedToken != null
                    && storedToken.equals(token)
                    && extractedOpenId.equals(openId)
                    && !isTokenExpired(token);

            if (!isValidToken) {
                log.warn("Open ID为{}的用户使用的{} Token无效或过期", openId, tokenType.getType());
            }

            return isValidToken;
        } catch (ExpiredJwtException e) {
            log.warn("Open ID为{}的用户{} Token已过期", openId, tokenType.getType());
            return false;
        }
    }

    /**
     * 构建Token的Redis Key
     *
     * @param openId    openId
     * @param tokenType Token类型
     * @return Token的Redis Key
     */
    private String buildTokenRedisKey(String openId, TokenType tokenType) {
        return JWT_REDIS_KEY_PREFIX
                + RedisConstants.REDIS_KEY_CONNECTOR + tokenType.getType()
                + RedisConstants.REDIS_KEY_CONNECTOR + openId;
    }

    // ==================== 辅助私有方法 ====================

    /**
     * 解析Token获取所有声明
     *
     * @param token token
     * @return 所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查Token是否过期
     *
     * @param token token
     * @return 是：过期 / 否：未过期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 提取Token过期时间
     *
     * @param token token
     * @return Token过期时间
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
