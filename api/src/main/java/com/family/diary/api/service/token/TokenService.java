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

package com.family.diary.api.service.token;

import com.family.diary.api.service.token.model.TokenPair;

/**
 * Token服务接口
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
public interface TokenService {
    /**
     * 签发Access/Refresh Token
     *
     * @param openId openId
     * @return TokenPair
     */
    TokenPair issueTokens(String openId);

    /**
     * 从Token中解析openId
     *
     * @param token token
     * @return openId
     */
    String extractOpenId(String token);

    /**
     * 校验Access Token
     *
     * @param token  token
     * @param openId openId
     * @return true/false
     */
    boolean validateAccessToken(String token, String openId);

    /**
     * 校验Refresh Token
     *
     * @param token  token
     * @param openId openId
     * @return true/false
     */
    boolean validateRefreshToken(String token, String openId);

    /**
     * 使当前用户的所有Token失效
     *
     * @param openId openId
     */
    void invalidateAllTokens(String openId);
}
