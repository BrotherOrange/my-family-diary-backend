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

package com.family.diary.api.service.tencentcloud;

import com.family.diary.api.dto.request.tencentcloud.cos.COSAvatarUploadRequest;
import com.family.diary.common.exceptions.BaseException;

/**
 * COS对象服务Service
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
public interface COSService {
    /**
     * 上传头像到腾讯云COS
     *
     * @param request 包含上传信息的请求对象
     * @return 上传后的临时图片URL
     * @throws BaseException 如果上传失败或发生其他错误
     */
    String uploadAvatarToCOS(COSAvatarUploadRequest request) throws BaseException;

    /**
     * 获取用户头像的临时URL
     *
     * @param openid 用户的唯一标识
     * @return 用户头像的URL
     */
    String getAvatarUrl(String openid);
}
