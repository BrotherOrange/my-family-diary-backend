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

package com.family.diary.api.dto.validation.common.annotation;

import com.family.diary.api.dto.validation.common.validator.ChinesePhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解用于验证中国大陆手机号格式
 *
 * @author Richard Zhang
 * @since 2025-07-28
 */
@Documented
@Constraint(validatedBy = ChinesePhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChinesePhone {
    /**
     * 提示信息
     *
     * @return 提示信息内容
     */
    String message() default "手机号格式不正确";

    /**
     * 指定校验所属的分组（JSR-380 分组校验机制）。
     *
     * @return 校验分组类数组
     */
    Class<?>[] groups() default {};

    /**
     * 与注解关联的元数据负载（Payload），可用于传递自定义元信息。
     *
     * @return Payload 类型数组
     */
    Class<? extends Payload>[] payload() default {};
}
