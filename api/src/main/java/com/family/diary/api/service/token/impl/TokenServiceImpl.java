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

package com.family.diary.api.service.token.impl;

import com.family.diary.api.service.token.TokenService;
import com.family.diary.api.service.token.model.TokenPair;
import com.family.diary.common.utils.web.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Token服务实现类
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;

    @Override
    public TokenPair issueTokens(String openId) {
        String accessToken = jwtUtil.generateAccessToken(openId);
        String refreshToken = jwtUtil.generateRefreshToken(openId);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public String extractOpenId(String token) {
        return jwtUtil.extractOpenId(token);
    }

    @Override
    public boolean validateAccessToken(String token, String openId) {
        return jwtUtil.validateAccessToken(token, openId);
    }

    @Override
    public boolean validateRefreshToken(String token, String openId) {
        return jwtUtil.validateRefreshToken(token, openId);
    }

    @Override
    public void invalidateAllTokens(String openId) {
        jwtUtil.invalidateAllTokens(openId);
    }
}
