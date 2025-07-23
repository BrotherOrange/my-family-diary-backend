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

package com.family.diary.common.models.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Redis Lettuce连接池属性值
 *
 * @author Richard Zhang
 * @since 2025-07-23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.lettuce.pool")
public class PoolProperties {
    /**
     * 是否启用连接池
     */
    private boolean enabled;

    /**
     * 最大空闲连接数
     */
    private int maxIdle;

    /**
     * 最小空闲连接数
     */
    private int minIdle;

    /**
     * 获取最大等待时间
     */
    private Duration maxWait;
}
