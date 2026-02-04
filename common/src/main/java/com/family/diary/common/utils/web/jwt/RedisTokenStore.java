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

package com.family.diary.common.utils.web.jwt;

import com.family.diary.common.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis Token存储实现
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisTokenStore implements TokenStore {

    private final RedisUtil redisUtil;

    @Override
    public void save(String key, String token, long expireMs) {
        redisUtil.setWithExpire(key, token, expireMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(String key) {
        var value = redisUtil.get(key);
        return value != null ? String.valueOf(value) : null;
    }

    @Override
    public void delete(String key) {
        redisUtil.delete(key);
    }
}
