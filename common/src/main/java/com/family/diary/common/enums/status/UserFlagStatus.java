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

package com.family.diary.common.enums.status;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户账户状态枚举类
 */
@Getter
public enum UserFlagStatus {
    /**
     * 正常状态
     */
    NORMAL("normal"),

    /**
     * 账户被冻结
     */
    FROZEN("frozen"),

    /**
     * 账户已注销
     */
    DELETED("deleted");

    /**
     * 枚举与描述的映射关系
     */
    private static final Map<String, UserFlagStatus> DESCRIPTION_MAP =
            Stream.of(values())
                    .collect(Collectors.toUnmodifiableMap(
                            UserFlagStatus::getDescription,
                            status -> status
                    ));

    private final String description;

    UserFlagStatus(String description) {
        this.description = description;
    }

    /**
     * 通过description获取对应的枚举实例，找不到则抛出异常
     *
     * @param description 描述信息
     * @return 对应的枚举实例
     * @throws IllegalArgumentException 如果没有找到匹配的枚举
     */
    public static UserFlagStatus getByDescription(String description) throws IllegalArgumentException {
        UserFlagStatus status = DESCRIPTION_MAP.get(description);
        if (status == null) {
            throw new IllegalArgumentException("无效的用户状态描述: " + description);
        }
        return status;
    }
}
