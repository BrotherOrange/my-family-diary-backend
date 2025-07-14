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

package com.family.diary.api.dto.request.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信账户信息查询请求体
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatAccountInfoQueryRequest {
    /**
     * 小程序登录凭证 code
     */
    private String code;

    /**
     * 加密算法的初始向量
     */
    private String iv;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;
}
