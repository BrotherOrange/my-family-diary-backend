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
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.common.CommonResponse;
import com.family.diary.domain.entity.tencentcloud.cos.COSAvatarUploadEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * COS对象存储服务Controller
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
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
     * @param request 上传头像请求体
     * @return 头像临时链接
     */
    @PostMapping("/avatar/upload")
    public CommonResponse<String> uploadAvatar(@RequestBody @Valid COSAvatarUploadRequest request) {
        log.info("开始上传头像");
        var entity = cosAvatarUploadMapper.toCOSAvatarUploadEntity(request);
        try {
            var tempAvatarUrl = cosService.uploadAvatarToCOS(entity);
            return CommonResponse.ok(tempAvatarUrl);
        } catch (BaseException e) {
            log.error("头像上传失败", e);
            return CommonResponse.fail(e.getMessage());
        }
    }

    /**
     * 获取头像临时链接
     *
     * @param openId 用户Open ID
     * @return 头像临时链接
     */
    @GetMapping("/avatar/url")
    public CommonResponse<String> getAvatarUrl(
            @RequestParam @Valid @NotEmpty(message = "openid不能为空") String openId) {
        log.info("开始获取头像URL");
        var tempAvatarUrl = cosService.getAvatarUrl(openId);
        return CommonResponse.ok(tempAvatarUrl);
    }
}
