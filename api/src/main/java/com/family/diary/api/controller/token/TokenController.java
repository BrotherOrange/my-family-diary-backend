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

package com.family.diary.api.controller.token;

import com.family.diary.api.dto.request.token.TokenRefreshRequest;
import com.family.diary.api.dto.response.token.TokenRefreshResponse;
import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.utils.common.CommonResponse;
import com.family.diary.common.utils.web.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Token管理Controller
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
@Tag(name = "Token管理", description = "Token刷新相关接口")
@Slf4j
@RestController
@Validated
@RequestMapping("/v1/token")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenController {

    private final JwtUtil jwtUtil;

    /**
     * 刷新Token接口
     * 使用Refresh Token获取新的Access Token和Refresh Token（滑动续期）
     *
     * @param request Token刷新请求
     * @return CommonResponse<TokenRefreshResponse>
     */
    @Operation(summary = "刷新Token", description = "使用Refresh Token获取新的Access Token和Refresh Token（滑动续期），无需Bearer Token认证")
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<TokenRefreshResponse>> refreshToken(
            @RequestBody @Valid TokenRefreshRequest request) {
        log.info("收到Token刷新请求");

        String refreshToken = request.getRefreshToken();
        String openId;

        // 从Refresh Token中提取openId
        try {
            openId = jwtUtil.extractOpenId(refreshToken);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh Token已过期");
            return CommonResponse.fail(ResponseErrorCode.UNAUTHORIZED, "Refresh Token已过期，请重新登录");
        } catch (MalformedJwtException e) {
            log.warn("Refresh Token格式无效");
            return CommonResponse.fail(ResponseErrorCode.UNAUTHORIZED, "Refresh Token无效");
        } catch (Exception e) {
            log.warn("Refresh Token解析失败", e);
            return CommonResponse.fail(ResponseErrorCode.UNAUTHORIZED, "Refresh Token无效");
        }

        // 验证Refresh Token
        if (!jwtUtil.validateRefreshToken(refreshToken, openId)) {
            log.warn("Refresh Token验证失败, openId: {}", openId);
            return CommonResponse.fail(ResponseErrorCode.UNAUTHORIZED, "Refresh Token无效或已过期，请重新登录");
        }

        // 生成新的Access Token和Refresh Token（滑动续期）
        String newAccessToken = jwtUtil.generateAccessToken(openId);
        String newRefreshToken = jwtUtil.generateRefreshToken(openId);

        log.info("Token刷新成功, openId: {}", openId);

        TokenRefreshResponse response = TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        return CommonResponse.ok(response);
    }
}
