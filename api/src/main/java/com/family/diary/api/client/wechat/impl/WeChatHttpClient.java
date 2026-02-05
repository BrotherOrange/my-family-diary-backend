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

package com.family.diary.api.client.wechat.impl;

import com.family.diary.api.client.wechat.WeChatClient;
import com.family.diary.api.dto.response.wechat.WeChatSessionResponse;
import com.family.diary.common.utils.common.RetryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

/**
 * 微信HTTP客户端实现
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WeChatHttpClient implements WeChatClient {

    private final RestTemplate restTemplate;

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Value("${wechat.code-to-session-url}")
    private String codeToSessionUrl;

    @Value("${wechat.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${wechat.retry.base-backoff-ms:200}")
    private long baseBackoffMs;

    @Override
    public WeChatSessionResponse code2Session(String code) {
        return RetryExecutor.execute("微信code2session", maxAttempts, baseBackoffMs, () -> {
            var requestUrl = codeToSessionUrl + "?appid=" + appId +
                    "&secret=" + appSecret +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            var url = URI.create(requestUrl);
            var headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            var httpEntity = new HttpEntity<>(headers);
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity,
                    WeChatSessionResponse.class).getBody();
        });
    }
}
