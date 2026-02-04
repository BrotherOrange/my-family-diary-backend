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

package com.family.diary.api.service.app.impl;

import com.family.diary.api.dto.response.user.UserCheckResponse;
import com.family.diary.api.mapper.user.UserApiMapper;
import com.family.diary.api.service.app.UserAppService;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户应用服务实现类
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAppServiceImpl implements UserAppService {

    private final UserService userService;

    private final UserApiMapper userApiMapper;

    private final COSService cosService;

    @Override
    public UserCheckResponse checkUserExists(String openId) {
        log.info("检查用户注册状态, openId: {}", openId);
        var user = userService.findByOpenId(openId);
        if (user == null) {
            log.info("OpenID为 {} 的用户未注册", openId);
            return UserCheckResponse.builder()
                    .registered(false)
                    .build();
        }
        log.info("OpenID为 {} 的用户已注册，用户名: {}", openId, user.getUsername());
        var response = userApiMapper.toUserCheckResponse(user);
        var avatarUrl = cosService.getAvatarUrl(openId);
        response.setAvatarUrl(avatarUrl);
        return response;
    }
}
