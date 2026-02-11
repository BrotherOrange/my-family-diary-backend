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

package com.family.diary.api.controller.tencentcloud;

import com.family.diary.api.dto.request.tencentcloud.cos.COSAvatarUploadRequest;
import com.family.diary.api.mapper.tencentcloud.cos.COSAvatarUploadMapper;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.common.utils.common.CommonResponse;
import com.family.diary.domain.entity.user.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * COS对象存储服务Controller
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Tag(name = "对象存储", description = "腾讯云COS文件上传相关接口")
@Slf4j
@RestController
@Validated
@RequestMapping("/v1/cos")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class COSController {
    private final COSService cosService;

    private final COSAvatarUploadMapper cosAvatarUploadMapper;

    /**
     * 上传头像
     *
     * @param currentUser 当前认证用户
     * @param request     上传头像请求体
     * @return 头像临时链接
     */
    @Operation(summary = "上传头像", description = "上传用户头像到腾讯云COS，返回临时访问链接")
    @PostMapping("/avatar/upload")
    public ResponseEntity<CommonResponse<String>> uploadAvatar(
            @AuthenticationPrincipal UserEntity currentUser,
            @RequestBody @Valid COSAvatarUploadRequest request) {
        log.info("开始上传头像");
        var entity = cosAvatarUploadMapper.toCOSAvatarUploadEntity(request);
        entity.setOpenId(currentUser.getOpenId());
        var tempAvatarUrl = cosService.uploadAvatarToCOS(entity);
        return CommonResponse.ok(tempAvatarUrl);
    }

    /**
     * 获取头像临时链接
     *
     * @param currentUser 当前认证用户
     * @return 头像临时链接
     */
    @Operation(summary = "获取头像链接", description = "获取当前用户头像的临时访问链接")
    @GetMapping("/avatar/url")
    public ResponseEntity<CommonResponse<String>> getAvatarUrl(
            @AuthenticationPrincipal UserEntity currentUser) {
        log.info("开始获取头像URL");
        var tempAvatarUrl = cosService.getAvatarUrl(currentUser.getOpenId());
        return CommonResponse.ok(tempAvatarUrl);
    }
}
