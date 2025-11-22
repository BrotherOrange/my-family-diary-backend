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

import com.family.diary.api.dto.response.wechat.WeChatUserEncryptedDataResponse;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.domain.entity.wechat.WeChatAccountInfoQueryEntity;

/**
 * 微信账户服务接口类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
public interface WeChatAccountService {
    /**
     * 获取微信账户信息
     *
     * @param entity 查询实体
     * @return 微信账户信息返回体
     * @throws BaseException 查询出现未知错误，进行捕获返回
     */
    WeChatUserEncryptedDataResponse getWeChatAccountInfo(WeChatAccountInfoQueryEntity entity) throws BaseException;
}
