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

import com.family.diary.api.dto.request.user.UserLoginRequest;
import com.family.diary.api.dto.request.user.UserRegisterRequest;
import com.family.diary.api.dto.response.user.UserLoginResponse;
import com.family.diary.api.dto.response.user.UserRegisterResponse;
import com.family.diary.api.mapper.user.UserApiMapper;
import com.family.diary.api.service.app.AuthAppService;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.api.service.token.TokenService;
import com.family.diary.api.service.token.model.TokenPair;
import com.family.diary.api.service.user.AuthService;
import com.family.diary.common.exceptions.UnauthorizedException;
import com.family.diary.domain.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证应用服务实现类
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthAppServiceImpl implements AuthAppService {

    private final AuthService authService;

    private final UserApiMapper userApiMapper;

    private final TokenService tokenService;

    private final COSService cosService;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        log.info("用户发起注册请求, openId: {}", request.getOpenId());
        var userEntity = userApiMapper.toUserEntity(request);
        var created = authService.register(userEntity);
        return userApiMapper.toUserRegisterResponse(created);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        log.info("用户发起登录请求, openId: {}", request.getOpenId());
        var user = authService.login(request.getOpenId(), request.getPassword());
        var response = userApiMapper.toUserLoginResponse(user);

        TokenPair tokens = tokenService.issueTokens(user.getOpenId());
        response.setAccessToken(tokens.accessToken());
        response.setRefreshToken(tokens.refreshToken());

        // 获取头像URL
        var avatarUrl = cosService.getAvatarUrl(user.getOpenId());
        response.setAvatarUrl(avatarUrl);

        return response;
    }

    @Override
    public void logout(UserEntity currentUser) {
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        String openId = currentUser.getOpenId();
        log.info("用户发起登出请求, openId: {}", openId);
        tokenService.invalidateAllTokens(openId);
        log.info("用户登出成功, openId: {}", openId);
    }
}
