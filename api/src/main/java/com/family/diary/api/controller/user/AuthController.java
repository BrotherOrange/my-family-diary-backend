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
import com.family.diary.api.service.app.AuthAppService;
import com.family.diary.common.utils.common.CommonResponse;
import com.family.diary.domain.entity.user.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RestController
@Validated
@RequestMapping("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final AuthAppService authAppService;

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
        var response = authAppService.register(userRegisterRequest);
        return CommonResponse.ok(response);
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
        var response = authAppService.login(userLoginRequest);
        return CommonResponse.ok(response);
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
        authAppService.logout(currentUser);
        return CommonResponse.ok(null);
    }
}
