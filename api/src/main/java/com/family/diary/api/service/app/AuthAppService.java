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

package com.family.diary.api.service.app;

import com.family.diary.api.dto.request.user.UserLoginRequest;
import com.family.diary.api.dto.request.user.UserRegisterRequest;
import com.family.diary.api.dto.response.user.UserLoginResponse;
import com.family.diary.api.dto.response.user.UserRegisterResponse;
import com.family.diary.domain.entity.user.UserEntity;

/**
 * 认证应用服务接口
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
public interface AuthAppService {

    /**
     * 注册
     *
     * @param request 注册请求
     * @return 注册响应
     */
    UserRegisterResponse register(UserRegisterRequest request);

    /**
     * 登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 登出
     *
     * @param currentUser 当前用户
     */
    void logout(UserEntity currentUser);
}
