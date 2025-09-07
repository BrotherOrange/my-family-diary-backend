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

package com.family.diary.infrastructure.po.community.post;

import com.baomidou.mybatisplus.annotation.TableName;
import com.family.diary.infrastructure.po.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 帖子持久化对象
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
@TableName("post")
public class PostPo extends BasePo {
    /**
     * 作者用户ID
     */
    private Long authorId;

    /**
     * 作者用户微信Open ID
     */
    private String authorOpenId;

    /**
     * 帖子类别ID
     */
    private Long categoryId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子副标题
     */
    private String subTitle;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子状态
     */
    private String status;
}
