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

    private final String description;

    UserFlagStatus(String description) {
        this.description = description;
    }
}
