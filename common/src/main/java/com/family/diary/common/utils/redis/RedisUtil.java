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

package com.family.diary.common.utils.redis;

import com.family.diary.common.constants.redis.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类，提供了一系列便捷的方法来操作Redis数据库。
 *
 * @author Richard Zhang
 * @since 2025-07-23
 */
@Slf4j
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long>             releaseLockScript;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.releaseLockScript = RedisScript.of(new ClassPathResource(RedisConstants.REDIS_RELEASE_LOCK_LUA_PATH),
                Long.class);
    }

    /**
     * 设置键值对到Redis中。
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置带有过期时间的键值对到Redis中。
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 操作是否成功
     */
    public boolean setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error("Redis setWithExpire error", e);
            return false;
        }
    }

    /**
     * 获取指定键对应的值。
     *
     * @param key 键
     * @return 键对应的值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定的键。
     *
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断指定的键是否存在。
     *
     * @param key 键
     * @return 如果键存在返回true，否则返回false
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 在哈希表中设置一个键值对。
     *
     * @param key     哈希表的键
     * @param hashKey 哈希表中的字段
     * @param value   字段对应的值
     */
    public void hashSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取哈希表中指定字段的值。
     *
     * @param key     哈希表的键
     * @param hashKey 哈希表中的字段
     * @return 字段对应的值
     */
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 向列表左侧添加元素。
     *
     * @param key   列表的键
     * @param value 元素值
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从列表左侧弹出一个元素。
     *
     * @param key 列表的键
     * @return 弹出的元素值
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 向集合中添加元素。
     *
     * @param key    集合的键
     * @param values 元素值
     */
    public void setAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合中的所有成员。
     *
     * @param key 集合的键
     * @return 集合的所有成员
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 添加元素到有序集合中，并设置分数。
     *
     * @param key   有序集合的键
     * @param value 元素值
     * @param score 分数
     */
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 根据分数范围获取有序集合中的成员。
     *
     * @param key 有序集合的键
     * @param min 最小分数
     * @param max 最大分数
     * @return 符合条件的成员集
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 尝试获取分布式锁。
     *
     * @param lockKey    锁的键
     * @param requestId  请求ID
     * @param expireTime 锁的过期时间
     * @param unit       时间单位
     * @return 如果获取锁成功返回true，否则返回false
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, unit));
    }

    /**
     * 释放分布式锁。
     *
     * @param lockKey   锁的键
     * @param requestId 请求ID
     */
    public void releaseLock(String lockKey, String requestId) {
        List<String> keys = List.of(lockKey);
        Long result = redisTemplate.execute(releaseLockScript, keys, requestId);
        if (result == 0L) {
            log.warn("Redis未能成功释放锁或锁不存在");
        }
    }

    /**
     * 执行Lua脚本。
     *
     * @param script Lua脚本内容
     * @param keys   键名列表
     * @param args   参数列表
     * @param <T>    返回类型
     * @return 脚本执行结果
     */
    public <T> T executeScript(String script, List<String> keys, Object... args) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType((Class<T>) Object.class);
        return redisTemplate.execute(redisScript, keys, args);
    }
}
