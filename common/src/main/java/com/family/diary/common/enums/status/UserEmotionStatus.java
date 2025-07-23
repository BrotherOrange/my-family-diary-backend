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

    UserEmotionStatus(String description) {
        this.description = description;
    }
}
