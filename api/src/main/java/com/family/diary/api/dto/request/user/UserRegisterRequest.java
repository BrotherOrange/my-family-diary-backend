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

package com.family.diary.api.dto.request.user;

import com.family.diary.api.dto.validation.common.annotation.ChinesePhone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 用户注册请求体
 *
 * @author Richard Zhang
 * @since 2025-07-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterRequest {
    /**
     * 微信账户OpenID
     */
    @NotBlank
    private String openId;

    /**
     * 家庭ID
     */
    @NotBlank
    private String familyId;

    /**
     * 用户名
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String username;

    /**
     * 密码
     */
    @NotBlank
    @Size(min = 8, max = 30)
    private String password;

    /**
     * 生日
     */
    @PastOrPresent
    private LocalDate birthday;

    /**
     * 手机号
     */
    @ChinesePhone
    private String phone;

    /**
     * 个人简介
     */
    @Size(max = 200)
    private String description;
}
