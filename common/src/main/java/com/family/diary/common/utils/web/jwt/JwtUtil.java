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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    /**
     * 生成JWT Token
     *
     * @param openId 微信OpenID
     * @return JWT Token String
     */
    public String generateToken(String openId) {
        return Jwts.builder()
                .setSubject(openId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWTConstants.EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512) // 使用HS512算法
                .compact();
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
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
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
        final String extractedOpenId = extractOpenId(token);
        return (extractedOpenId.equals(openId) && !isTokenExpired(token));
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

