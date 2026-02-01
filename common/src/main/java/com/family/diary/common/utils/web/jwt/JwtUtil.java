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
import com.family.diary.common.utils.redis.RedisUtil;
import io.jsonwebtoken.Claims;
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

    /**
     * 生成JWT Token
     *
     * @param openId 微信OpenID
     * @return JWT Token String
     */
    public String generateToken(String openId) {
        var token = Jwts.builder()
                .setSubject(openId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWTConstants.EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512) // 使用HS512算法
                .compact();
        var redisKey = buildJwtRedisTokenKey(openId);
        redisUtil.delete(redisKey);
        redisUtil.setWithExpire(redisKey, token, JWTConstants.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

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

    /**
     * 提取JwtToken的Redis Key值
     *
     * @param openId openId
     * @return JwtToken的Redis Key值
     */
    public String buildJwtRedisTokenKey(String openId) {
        return JWT_REDIS_KEY_PREFIX + RedisConstants.REDIS_KEY_CONNECTOR + openId;
    }

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
     * 验证Token是否有效
     *
     * @param token  token
     * @param openId 微信OpenID
     * @return 是：有效 / 否：无效
     */
    public Boolean validateToken(String token, String openId) {
        final var extractedOpenId = extractOpenId(token);
        var isValidToken = redisUtil.get(buildJwtRedisTokenKey(openId)).equals(token) && extractedOpenId.equals(openId)
                && !isTokenExpired(token);
        if (!isValidToken) {
            log.warn("Open ID为{}的用户使用的token无效或过期", openId);
        }
        return isValidToken;
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

