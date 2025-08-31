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

import com.family.diary.common.exceptions.database.InsertException;
import com.family.diary.common.exceptions.database.QueryException;
import com.family.diary.domain.entity.user.UserEntity;

public interface AuthService {
    /**
     * 用户注册
     *
     * @param user 用户实体
     * @return 被创建的用户实体
     * @throws QueryException 查询异常
     * @throws InsertException 插入异常
     */
    UserEntity register(UserEntity user) throws QueryException, InsertException;

    /**
     * 用户登录
     *
     * @param openId 微信openId
     * @param password 用户明文密码
     * @return UserEntity 登录的用户实体
     * @throws QueryException 查询异常
     */
    UserEntity login(String openId, String password) throws QueryException;
}
