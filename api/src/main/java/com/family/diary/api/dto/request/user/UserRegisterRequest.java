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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "用户注册请求")
public class UserRegisterRequest {

    @Schema(description = "微信OpenId", example = "oXxx_xxxxxxxxxxxxx", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "微信Open ID不得为空")
    private String openId;

    @Schema(description = "用户名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不得为空")
    @Size(min = 1, max = 20, message = "用户名长度不能超过20个字符")
    private String username;

    @Schema(description = "用户密码", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不得为空")
    @Size(min = 8, max = 30, message = "密码长度必须为8-30位之间")
    private String password;

    @Schema(description = "生日", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生日不得为空")
    @PastOrPresent(message = "生日时间不得晚于当前时间")
    private LocalDate birthday;

    @Schema(description = "手机号", example = "13800138000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不得为空")
    @ChinesePhone(message = "手机号格式不合法")
    private String phone;

    @Schema(description = "个人简介", example = "这是我的个人简介")
    @Size(max = 200, message = "个人简介不得超过200个字符")
    private String description;
}
