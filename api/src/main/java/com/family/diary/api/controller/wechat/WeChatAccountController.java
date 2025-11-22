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

package com.family.diary.api.controller.wechat;

import com.family.diary.api.dto.request.wechat.WeChatAccountInfoQueryRequest;
import com.family.diary.api.dto.response.wechat.WeChatUserEncryptedDataResponse;
import com.family.diary.api.mapper.wechat.WeChatAccountInfoQueryMapper;
import com.family.diary.api.service.wechat.WeChatAccountService;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信账户相关Controller
 *
 * @author Richard Zhang
 * @since 2025-07-12
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/v1/wechat/account")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeChatAccountController {
    private final WeChatAccountService weChatAccountService;

    private final WeChatAccountInfoQueryMapper weChatAccountInfoQueryMapper;

    /**
     * 获取微信用户信息
     *
     * @param request request
     * @return WeChatUserEncryptedDataResponse
     */
    @PostMapping("/info")
    public CommonResponse<WeChatUserEncryptedDataResponse> getWeChatAccountInfo(
            @RequestBody @Valid WeChatAccountInfoQueryRequest request) {
        var entity = weChatAccountInfoQueryMapper.toWeChatAccountInfoQueryEntity(request);
        try {
            var response = weChatAccountService.getWeChatAccountInfo(entity);
            log.info("获取微信用户信息成功，用户ID: {}", response.getOpenId());
            return CommonResponse.ok(response);
        } catch (BaseException e) {
            log.error("获取微信用户信息失败，错误信息: {}", e.getMessage());
            return CommonResponse.fail(e.getMessage());
        }
    }
}
