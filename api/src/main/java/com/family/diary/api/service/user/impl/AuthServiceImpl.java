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

import com.family.diary.api.service.user.AuthService;
import com.family.diary.api.service.user.UserService;
import com.family.diary.common.exceptions.ConflictException;
import com.family.diary.common.exceptions.UnauthorizedException;
import com.family.diary.common.utils.common.PasswordUtil;
import com.family.diary.domain.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户认证服务实现类
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    private final PasswordUtil passwordUtil;

    @Override
    public UserEntity register(UserEntity user) {
        // 给用户密码加密
        user.setPassword(passwordUtil.encode(user.getPassword()));

        // 查询用户是否已存在
        var createdUser = userService.findByOpenId(user.getOpenId());
        if (createdUser != null) {
            log.error("OpenID为 {} 的用户已存在，中止注册流程", user.getOpenId());
            throw new ConflictException("用户已存在，无法重复注册！");
        }

        // 创建新用户
        return userService.create(user);
    }

    @Override
    public UserEntity login(String openId, String password) {
        var user = userService.findByOpenId(openId);
        if (user == null) {
            log.error("OpenID为 {} 的用户不存在，登录失败", openId);
            throw new UnauthorizedException("用户不存在，登录失败！");
        }
        if (!passwordUtil.matches(password, user.getPassword())) {
            log.error("OpenID为 {} 的用户密码错误，登录失败", openId);
            throw new UnauthorizedException("用户密码错误，登录失败！");
        }
        log.info("OpenID为 {} 的用户登录成功", openId);
        return user;
    }
}
