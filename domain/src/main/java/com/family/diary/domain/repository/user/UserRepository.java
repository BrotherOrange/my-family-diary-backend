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

package com.family.diary.domain.repository.user;

import com.family.diary.domain.entity.user.UserEntity;

/**
 * UserRepository接口类
 *
 * @author Richard Zhang
 * @since 2025-07-19
 */
public interface UserRepository {
    /**
     * 保存新用户
     *
     * @param user 用户实体
     * @return 保存的记录数：1成功，0失败
     */
    int save(UserEntity user);

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
