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

package com.family.diary.api.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户登录响应体
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "用户登录响应")
public class UserLoginResponse {

    @Schema(description = "用户OpenID", example = "oXxx_xxxxxxxxxxxxx")
    private String openId;

    @Schema(description = "用户名", example = "张三")
    private String username;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "个人简介", example = "这是我的个人简介")
    private String description;

    @Schema(description = "生日", example = "1990-01-01")
    private String birthday;

    @Schema(description = "个人状态", example = "正常")
    private String status;

    @Schema(description = "头像链接", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Access Token（15分钟有效）", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Refresh Token（7天有效）", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;
}
