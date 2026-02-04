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

package com.family.diary.api.controller.user;

import com.family.diary.api.dto.request.user.UserLoginRequest;
import com.family.diary.api.dto.request.user.UserRegisterRequest;
import com.family.diary.api.dto.response.user.UserLoginResponse;
import com.family.diary.api.dto.response.user.UserRegisterResponse;
import com.family.diary.api.mapper.user.UserApiMapper;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.api.service.user.AuthService;
import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.exceptions.database.InsertException;
import com.family.diary.common.exceptions.database.QueryException;
import com.family.diary.common.factories.common.GsonFactory;
import com.family.diary.common.utils.common.CommonResponse;
import com.family.diary.common.utils.web.jwt.JwtUtil;
import com.family.diary.domain.entity.user.UserEntity;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证Controller
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
@Tag(name = "用户认证", description = "登录、注册、登出相关接口")
@Slf4j
@RestController
@Validated
@RequestMapping("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final Gson gson = GsonFactory.createGson();

    private final AuthService authService;

    private final JwtUtil jwtUtil;

    private final UserApiMapper userApiMapper;

    private final COSService cosService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册请求
     * @return CommonResponse<UserRegisterResponse>
     */
    @Operation(summary = "用户注册", description = "新用户注册接口，需要提供微信OpenID、用户名、密码等信息")
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<UserRegisterResponse>> register(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        log.info("用户发起注册请求: {}", gson.toJson(userRegisterRequest));
        var userRequestEntity = userApiMapper.toUserEntity(userRegisterRequest);
        try {
            var userResponseEntity = authService.register(userRequestEntity);
            return CommonResponse.ok(userApiMapper.toUserRegisterResponse(userResponseEntity));
        } catch (QueryException | InsertException e) {
            log.error("用户注册失败！");
            return CommonResponse.fail(ResponseErrorCode.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录请求
     * @return CommonResponse<UserLoginResponse>
     */
    @Operation(summary = "用户登录", description = "用户登录接口，成功后返回Access Token和Refresh Token")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<UserLoginResponse>> login(
            @RequestBody @Valid UserLoginRequest userLoginRequest) {
        log.info("用户发起登录请求: {}", gson.toJson(userLoginRequest));
        try {
            var user = authService.login(userLoginRequest.getOpenId(), userLoginRequest.getPassword());
            var userLoginResponse = userApiMapper.toUserLoginResponse(user);
            // 生成双Token
            String accessToken = jwtUtil.generateAccessToken(user.getOpenId());
            String refreshToken = jwtUtil.generateRefreshToken(user.getOpenId());
            userLoginResponse.setAccessToken(accessToken);
            userLoginResponse.setRefreshToken(refreshToken);
            userLoginResponse.setOpenId(user.getOpenId());
            // 获取头像URL
            var avatarUrl = cosService.getAvatarUrl(user.getOpenId());
            userLoginResponse.setAvatarUrl(avatarUrl);
            return CommonResponse.ok(userLoginResponse);
        } catch (QueryException | InsertException e) {
            log.error("用户登录失败");
            return CommonResponse.fail(ResponseErrorCode.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 用户登出接口
     * 使当前用户的所有Token失效
     *
     * @param currentUser 当前登录用户（由Spring Security注入）
     * @return CommonResponse<Void>
     */
    @Operation(summary = "用户登出", description = "登出接口，使当前用户的所有Token失效，需要Bearer Token认证")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(@AuthenticationPrincipal UserEntity currentUser) {
        if (currentUser == null) {
            log.warn("登出失败：无法获取当前用户信息");
            return CommonResponse.fail(ResponseErrorCode.UNAUTHORIZED, "用户未登录");
        }
        String openId = currentUser.getOpenId();
        log.info("用户发起登出请求, openId: {}", openId);
        jwtUtil.invalidateAllTokens(openId);
        log.info("用户登出成功, openId: {}", openId);
        return CommonResponse.ok(null);
    }
}
