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

import com.family.diary.api.dto.response.wechat.WeChatSessionResponse;
import com.family.diary.api.service.wechat.WeChatAccountService;
import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

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
    private final RestTemplate restTemplate;

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Value("${wechat.code-to-session-url}")
    private String codeToSessionUrl;

    @Override
    public String getOpenIdByCode(String code) throws BaseException {
        log.info("静默登录，code: {}", code);
        try {
            var requestUrl = codeToSessionUrl + "?appid=" + appId +
                    "&secret=" + appSecret +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            var url = new URI(requestUrl);
            var headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            var httpEntity = new HttpEntity<>(headers);
            WeChatSessionResponse sessionResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                    WeChatSessionResponse.class).getBody();
            log.info("静默登录成功，openId: {}", sessionResponse != null ? sessionResponse.getOpenId() : null);
            assert sessionResponse != null;
            return sessionResponse.getOpenId();
        } catch (Exception e) {
            log.error("静默登录失败，code: {}, 错误信息: {}", code, e.getMessage());
            throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "获取openId失败: " + e.getMessage());
        }
    }
}
