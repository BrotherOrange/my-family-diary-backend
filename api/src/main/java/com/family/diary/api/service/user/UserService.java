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

package com.family.diary.api.service.user;

import com.family.diary.domain.entity.user.UserEntity;

/**
 * 用户功能服务接口类
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
public interface UserService {
    /**
     * 创建新用户
     *
     * @param user 用户实体
     * @return 被创建的用户实体
     */
    UserEntity create(UserEntity user);

    /**
     * 通过用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    UserEntity findById(Long id);

    /**
     * 通过微信OpenID查询用户
     *
     * @param openId 微信用户OpenID
     * @return 用户实体
     */
    UserEntity findByOpenId(String openId);
}
