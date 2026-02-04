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

package com.family.diary.api.service.wechat.impl;

import com.family.diary.api.client.wechat.WeChatClient;
import com.family.diary.api.service.wechat.WeChatAccountService;
import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信账户服务实现类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WeChatAccountServiceImpl implements WeChatAccountService {
    private final WeChatClient weChatClient;

    @Override
    public String getOpenIdByCode(String code) throws BaseException {
        log.info("静默登录，code: {}", maskCode(code));
        try {
            var sessionResponse = weChatClient.code2Session(code);
            if (sessionResponse == null || sessionResponse.getOpenId() == null
                    || sessionResponse.getOpenId().isBlank()) {
                log.error("静默登录失败，未获取到OpenId");
                throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "获取openId失败");
            }
            log.info("静默登录成功，openId: {}", sessionResponse.getOpenId());
            return sessionResponse.getOpenId();
        } catch (Exception e) {
            log.error("静默登录失败，code: {}, 错误信息: {}", maskCode(code), e.getMessage());
            throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "获取openId失败: " + e.getMessage());
        }
    }

    private String maskCode(String code) {
        if (code == null || code.isBlank()) {
            return "null";
        }
        if (code.length() <= 6) {
            return "******";
        }
        return code.substring(0, 3) + "****" + code.substring(code.length() - 3);
    }
}
