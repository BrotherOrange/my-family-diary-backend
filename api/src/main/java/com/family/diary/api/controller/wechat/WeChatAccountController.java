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

import com.family.diary.api.dto.request.wechat.Code2SessionRequest;
import com.family.diary.api.dto.response.wechat.Code2SessionResponse;
import com.family.diary.api.service.wechat.WeChatAccountService;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /**
     * 静默登录 - 仅用code换取openId
     *
     * @param request request
     * @return Code2SessionResponse
     */
    @PostMapping("/code2session")
    public ResponseEntity<CommonResponse<Code2SessionResponse>> code2Session(
            @RequestBody @Valid Code2SessionRequest request) {
        try {
            String openId = weChatAccountService.getOpenIdByCode(request.getCode());
            log.info("静默登录成功，openId: {}", openId);
            return CommonResponse.ok(new Code2SessionResponse(openId));
        } catch (BaseException e) {
            log.error("静默登录失败，错误信息: {}", e.getMessage());
            return CommonResponse.fail(e.getMessage());
        }
    }
}
