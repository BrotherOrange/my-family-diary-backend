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

package com.family.diary.api.dto.request.tencentcloud.cos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传头像到COS请求体
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class COSAvatarUploadRequest {
    /**
     * 用户的 OpenID
     */
    private String openId;

    /**
     * 用户的头像 Base64 编码字符串
     * 注意：Base64 编码的字符串需要去掉前缀 "data:image/png;base64,"
     */
    private String base64Image;
}
