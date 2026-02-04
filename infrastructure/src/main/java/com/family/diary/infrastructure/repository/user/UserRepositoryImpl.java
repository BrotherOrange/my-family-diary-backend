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
import com.family.diary.common.exceptions.ConflictException;
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.domain.repository.user.UserRepository;
import com.family.diary.infrastructure.dao.user.UserDAO;
import com.family.diary.infrastructure.mapper.user.UserMapper;
import com.family.diary.infrastructure.po.user.UserPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * UserRepository实现类
 *
 * @author Richard Zhang
 * @since 2025-07-19
 */
@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryImpl implements UserRepository {
    private final UserDAO userDAO;

    private final UserMapper userMapper;

    @Override
    public int save(UserEntity user) {
        return userDAO.insert(userMapper.toUserPo(user));
    }

    @Override
    public UserEntity findById(Long id) {
        var userPo = userDAO.selectById(id);
        return userPo != null ? userMapper.toUserEntity(userPo) : null;
    }

    @Override
    public UserEntity findByOpenId(String openId) {
        var queryWrapper = new QueryWrapper<UserPo>().lambda().eq(UserPo::getOpenId, openId);
        try {
            var userPo = userDAO.selectOne(queryWrapper);
            return userPo != null ? userMapper.toUserEntity(userPo) : null;
        } catch (TooManyResultsException e) {
            log.error("OpenId {} 查询到多个用户", openId);
            throw new ConflictException("OpenId重复!");
        }
    }
}
