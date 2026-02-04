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

import com.family.diary.api.dto.response.user.UserCheckResponse;
import com.family.diary.api.mapper.user.UserApiMapper;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.api.service.user.UserService;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户功能Controller
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
@Tag(name = "用户管理", description = "用户信息查询相关接口")
@Slf4j
@RestController
@Validated
@RequestMapping("/v1/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    private final UserApiMapper userApiMapper;

    private final COSService cosService;

    /**
     * 检查用户是否已注册
     *
     * @param openId 微信openId
     * @return CommonResponse<UserCheckResponse>
     */
    @Operation(summary = "检查用户注册状态", description = "根据微信OpenID检查用户是否已注册，已注册则返回用户基本信息")
    @GetMapping("/exists")
    public ResponseEntity<CommonResponse<UserCheckResponse>> checkUserExists(
            @Parameter(description = "微信OpenID", required = true, example = "oXxx_xxxxxxxxxxxxx")
            @RequestParam("openId") @NotBlank(message = "Open ID不能为空") String openId) {
        log.info("检查用户注册状态, openId: {}", openId);
        var user = userService.findByOpenId(openId);
        if (user == null) {
            log.info("OpenID为 {} 的用户未注册", openId);
            return CommonResponse.ok(UserCheckResponse.builder()
                    .registered(false)
                    .build());
        }
        log.info("OpenID为 {} 的用户已注册，用户名: {}", openId, user.getUsername());
        var response = userApiMapper.toUserCheckResponse(user);
        // 获取头像URL
        var avatarUrl = cosService.getAvatarUrl(openId);
        response.setAvatarUrl(avatarUrl);
        return CommonResponse.ok(response);
    }
}
