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

package com.family.diary.api.mapper.tencentcloud.cos;

import com.family.diary.api.dto.request.tencentcloud.cos.COSAvatarUploadRequest;
import com.family.diary.domain.entity.tencentcloud.cos.COSAvatarUploadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * COS服务头像上传的映射接口类
 *
 * @author Richard Zhang
 * @since 2025-11-19
 */
@Mapper(componentModel = "spring")
public interface COSAvatarUploadMapper {
    COSAvatarUploadMapper INSTANCE = Mappers.getMapper(COSAvatarUploadMapper.class);

    /**
     * COSAvatarUploadRequest -> COSAvatarUploadEntity
     *
     * @param cosAvatarUploadRequest COSAvatarUploadRequest
     * @return COSAvatarUploadEntity
     */
    COSAvatarUploadEntity toCOSAvatarUploadEntity(COSAvatarUploadRequest cosAvatarUploadRequest);
}
