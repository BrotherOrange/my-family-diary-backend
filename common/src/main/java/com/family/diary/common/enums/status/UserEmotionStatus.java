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
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户情绪状态枚举
 * 该枚举定义了用户可能的情绪状态，便于在应用中进行情绪相关的处理和展示。
 *
 * @author Richard Zhang
 * @since 2025-07-16
 */
@Getter
public enum UserEmotionStatus {
    /**
     * 开心
     */
    HAPPY("开心"),

    /**
     * 难过
     */
    SAD("难过"),

    /**
     * 生气
     */
    ANGRY("生气"),

    /**
     * 惊讶
     */
    SURPRISED("惊讶"),

    /**
     * 害怕
     */
    SCARED("害怕"),

    /**
     * 平静
     */
    PEACE("平静"),

    /**
     * 紧张
     */
    NERVOUS("紧张"),

    /**
     * 疲惫
     */
    TIRED("疲惫"),

    /**
     * 兴奋
     */
    EXCITED("兴奋");

    private final String description;

    private static final Map<String, UserEmotionStatus> DESCRIPTION_TO_ENUM = new ConcurrentHashMap<>();

    UserEmotionStatus(String description) {
        this.description = description;
    }

    static {
        for (UserEmotionStatus status : values()) {
            DESCRIPTION_TO_ENUM.put(status.description, status);
        }
    }

    /**
     * 根据描述文本获取对应的枚举实例（高效 O(1) 查找）
     *
     * @param description 情绪的中文描述，如 "开心"、"难过" 等
     * @return 对应的 UserEmotionStatus 枚举实例
     * @throws IllegalArgumentException 如果没有找到匹配的枚举值
     */
    public static UserEmotionStatus fromDescription(String description) {
        UserEmotionStatus status = DESCRIPTION_TO_ENUM.get(description);
        if (status == null) {
            throw new IllegalArgumentException("No enum constant with description: " + description);
        }
        return status;
    }

    /**
     * 安全版本：根据描述文本获取对应的枚举实例，未找到时返回 null
     *
     * @param description 情绪的中文描述
     * @return 对应的枚举实例，若未找到则返回 null
     */
    public static UserEmotionStatus fromDescriptionOrNull(String description) {
        return DESCRIPTION_TO_ENUM.get(description);
    }
}
