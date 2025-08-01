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

package com.family.diary.common.constants.tencentcloud;

/**
 * COS对象存储服务常量类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
public interface COSConstants {
    /**
     * 临时密钥过期时间，单位为秒
     */
    Integer TEMP_TOKEN_EXPIRE_TIME = 7200;

    /**
     * 头像存储路径
     */
    String AVATARS_DIR = "images/user/avatars";

    /**
     * 头像缓存前缀
     */
    String AVATARS_CACHE_KEY_PREFIX = "avatar:url";
}
