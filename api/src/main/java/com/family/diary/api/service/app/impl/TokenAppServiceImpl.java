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

package com.family.diary.api.service.app.impl;

import com.family.diary.api.dto.request.token.TokenRefreshRequest;
import com.family.diary.api.dto.response.token.TokenRefreshResponse;
import com.family.diary.api.service.app.TokenAppService;
import com.family.diary.api.service.token.TokenService;
import com.family.diary.api.service.token.model.TokenPair;
import com.family.diary.common.constants.response.ResponseMessageConstants;
import com.family.diary.common.exceptions.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Token应用服务实现类
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TokenAppServiceImpl implements TokenAppService {

    private final TokenService tokenService;

    @Override
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        log.info("收到Token刷新请求");

        String refreshToken = request.getRefreshToken();
        String openId;

        try {
            openId = tokenService.extractOpenId(refreshToken);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh Token已过期");
            throw new UnauthorizedException(ResponseMessageConstants.REFRESH_TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            log.warn("Refresh Token格式无效");
            throw new UnauthorizedException(ResponseMessageConstants.REFRESH_TOKEN_INVALID);
        } catch (Exception e) {
            log.warn("Refresh Token解析失败", e);
            throw new UnauthorizedException(ResponseMessageConstants.REFRESH_TOKEN_INVALID);
        }

        if (!tokenService.validateRefreshToken(refreshToken, openId)) {
            log.warn("Refresh Token验证失败, openId: {}", openId);
            throw new UnauthorizedException(ResponseMessageConstants.REFRESH_TOKEN_INVALID);
        }

        TokenPair tokens = tokenService.issueTokens(openId);

        log.info("Token刷新成功, openId: {}", openId);

        return TokenRefreshResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .build();
    }
}
