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
import com.family.diary.api.service.app.TokenAppService;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RestController
@Validated
@RequestMapping("/v1/token")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenController {

    private final TokenAppService tokenAppService;

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
        var response = tokenAppService.refresh(request);
        return CommonResponse.ok(response);
    }
}
