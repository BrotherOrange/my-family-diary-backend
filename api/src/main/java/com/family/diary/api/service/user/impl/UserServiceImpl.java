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

package com.family.diary.api.service.user.impl;

import com.family.diary.api.service.user.UserService;
import com.family.diary.common.exceptions.database.InsertException;
import com.family.diary.common.exceptions.database.QueryException;
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户功能服务实现类
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity create(UserEntity user) throws QueryException, InsertException {
        var result = userRepository.save(user);
        if (result == 0) {
            log.error("创建用户失败: {}", user);
            throw new InsertException("User creation failed");
        }
        try {
            return findByOpenId(user.getOpenId());
        } catch (QueryException e) {
            log.error("查询已创建的用户失败: {}", user.getOpenId(), e);
            throw new QueryException("查询创建的用户失败");
        }
    }

    @Override
    public UserEntity findById(Long id) {
        return null;
    }

    @Override
    public UserEntity findByOpenId(String openId) throws QueryException {
        return userRepository.findByOpenId(openId);
    }
}
