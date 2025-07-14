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

import com.family.diary.api.dto.request.wechat.WeChatAccountInfoQueryRequest;
import com.family.diary.api.dto.response.wechat.WeChatSessionResponse;
import com.family.diary.api.dto.response.wechat.WeChatUserEncryptedDataResponse;
import com.family.diary.api.service.wechat.WeChatAccountService;
import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.wechat.WechatDataDecoder;
import com.nimbusds.jose.shaded.gson.Gson;
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

@Slf4j
@Service
public class WeChatAccountServiceImpl implements WeChatAccountService {
    private final WechatDataDecoder wechatDataDecoder;
    private final RestTemplate      restTemplate;
    private final Gson              gson = new Gson();

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Value("${wechat.code-to-session-url}")
    private String codeToSessionUrl;

    @Autowired
    public WeChatAccountServiceImpl(WechatDataDecoder wechatDataDecoder, RestTemplate restTemplate) {
        this.wechatDataDecoder = wechatDataDecoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public WeChatUserEncryptedDataResponse getWeChatAccountInfo(WeChatAccountInfoQueryRequest request)
            throws BaseException {
        log.info("获取微信用户信息，request: {}", gson.toJson(request));
        String code = request.getCode();
        String iv = request.getIv();
        String encryptedData = request.getEncryptedData();
        try {
            String requestUrl = codeToSessionUrl + "?appid=" + appId +
                    "&secret=" + appSecret +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            URI url = new URI(requestUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            WeChatSessionResponse sessionResponse = restTemplate.exchange(url, HttpMethod.GET, entity,
                    WeChatSessionResponse.class).getBody();
            log.info("获取微信用户会话信息成功，sessionResponse: {}", gson.toJson(sessionResponse));
            assert sessionResponse != null;
            String decryptedData = wechatDataDecoder.decryptData(encryptedData, sessionResponse.getSessionKey(), iv);
            log.info("获取解密的微信用户信息成功，decryptedData: {}", gson.toJson(decryptedData));
            return gson.fromJson(decryptedData, WeChatUserEncryptedDataResponse.class);
        } catch (Exception e) {
            log.error("获取微信用户信息失败，code: {}, 错误信息: {}", code, e.getMessage());
            throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "获取微信用户信息失败: " + e.getMessage());
        }
    }
}
