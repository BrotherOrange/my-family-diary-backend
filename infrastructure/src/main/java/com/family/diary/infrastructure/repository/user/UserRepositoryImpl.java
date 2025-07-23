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

package com.family.diary.infrastructure.repository.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.domain.repository.user.UserRepository;
import com.family.diary.infrastructure.dao.user.UserDAO;
import com.family.diary.infrastructure.mapper.user.UserMapper;
import com.family.diary.infrastructure.po.user.UserPo;
import jakarta.annotation.Resource;

/**
 * UserRepository实现类
 *
 * @author Richard Zhang
 * @since 2025-07-19
 */
public class UserRepositoryImpl implements UserRepository {
    @Resource
    private UserDAO userDAO;

    @Resource
    private UserMapper userMapper;

    @Override public void save(UserEntity user) {
        userDAO.insert(userMapper.toUserPo(user));
    }

    @Override public UserEntity findById(Long id) {
        return userMapper.toUserEntity(userDAO.selectById(id));
    }

    @Override public UserEntity findByOpenId(String openId) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<UserPo>().eq("open_id", openId);
        return userMapper.toUserEntity(userDAO.selectOne(queryWrapper));
    }
}
