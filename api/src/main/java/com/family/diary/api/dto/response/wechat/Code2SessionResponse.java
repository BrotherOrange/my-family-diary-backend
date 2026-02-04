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

package com.family.diary.api.dto.response.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 静默登录响应体
 *
 * @author Richard Zhang
 * @since 2026-02-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "微信静默登录响应")
public class Code2SessionResponse {

    @Schema(description = "用户唯一标识OpenID", example = "oXxx_xxxxxxxxxxxxx")
    private String openId;
}
