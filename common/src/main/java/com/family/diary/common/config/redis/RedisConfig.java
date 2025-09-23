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

package com.family.diary.common.config.redis;

import com.family.diary.common.constants.redis.RedisConstants;
import com.family.diary.common.models.redis.PoolProperties;
import com.family.diary.common.models.redis.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 *
 * @author Richard Zhang
 * @since 2025-07-23
 */
@Slf4j
@Configuration
public class RedisConfig {
    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    /**
     * Redis连接工厂配置
     *
     * @return RedisConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        var standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisProperties.getHost());
        standaloneConfig.setPort(redisProperties.getPort());
        if (!redisProperties.isVersionLessThan6()) {
            standaloneConfig.setUsername(redisProperties.getUsername());
        } else {
            log.warn("Redis版本：{}小于6.0，直接使用密码认证", redisProperties.getVersion());
        }
        standaloneConfig.setPassword(redisProperties.getPassword());

        var clientConfig = LettuceClientConfiguration.builder()
                .clientName(RedisConstants.REDIS_CLIENT_NAME)
                .build();

        // 如果启用了连接池
        if (isPoolEnabled()) {
            clientConfig = LettucePoolingClientConfiguration.builder()
                    .poolConfig(buildPoolConfig())
                    .build();
        }

        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }

    /**
     * Redis Template配置
     *
     * @param connectionFactory Redis连接工厂配置
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    private boolean isPoolEnabled() {
        var pool = redisProperties.getLettuce().getPool();
        return pool != null && pool.isEnabled();
    }

    private GenericObjectPoolConfig<?> buildPoolConfig() {
        var pool = redisProperties.getLettuce().getPool();

        var poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(16); // 默认最大连接数
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMaxWait(pool.getMaxWait());
        poolConfig.setTestWhileIdle(true);

        return poolConfig;
    }
}
