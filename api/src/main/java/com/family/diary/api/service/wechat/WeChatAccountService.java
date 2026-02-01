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

package com.family.diary.api.service.wechat;

import com.family.diary.common.exceptions.BaseException;

/**
 * 微信账户服务接口类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
public interface WeChatAccountService {
    /**
     * 静默登录 - 通过code获取openId
     *
     * @param code 小程序登录凭证
     * @return openId
     * @throws BaseException 获取失败时抛出异常
     */
    String getOpenIdByCode(String code) throws BaseException;
}
