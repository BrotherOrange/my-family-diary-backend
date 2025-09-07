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

package com.family.diary.infrastructure.po.family;

import com.baomidou.mybatisplus.annotation.TableName;
import com.family.diary.infrastructure.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 家庭持久化对象
 *
 * @author Richard Zhang
 * @since 2025-09-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("family")
public class FamilyPo extends BasePo {
    /**
     * 家庭创建者ID
     */
    private Long ownerId;

    /**
     * 家庭创建者微信Open ID
     */
    private String ownerOpenId;

    /**
     * 家庭名称
     */
    private String name;

    /**
     * 家庭描述
     */
    private String description;

    /**
     * 家庭状态
     */
    private String status;
}
