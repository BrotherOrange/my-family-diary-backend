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

package com.family.diary.api.mapper.user;

import com.family.diary.api.dto.request.user.UserRegisterRequest;
import com.family.diary.api.dto.response.user.UserLoginResponse;
import com.family.diary.api.dto.response.user.UserRegisterResponse;
import com.family.diary.common.enums.status.UserEmotionStatus;
import com.family.diary.common.mapper.CommonConvertMapper;
import com.family.diary.domain.entity.user.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * API层User映射类
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
@Mapper(componentModel = "spring", uses = { CommonConvertMapper.class })
public interface UserApiMapper {
    UserApiMapper INSTANCE = Mappers.getMapper(UserApiMapper.class);

    /**
     * UserRegisterRequest -> UserEntity
     *
     * @param userRegisterRequest userRegisterRequest
     * @return UserEntity
     */
    UserEntity toUserEntity(UserRegisterRequest userRegisterRequest);

    /**
     * UserEntity -> UserRegisterResponse
     *
     * @param userEntity userEntity
     * @return UserRegisterResponse
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToStringWithDefault")
    @Mapping(source = "birthday", target = "birthday", qualifiedByName = "localDateToString")
    UserRegisterResponse toUserRegisterResponse(UserEntity userEntity);

    /**
     * UserEntity -> UserLoginResponse
     *
     * @param userEntity userEntity
     * @return UserLoginResponse
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToStringWithDefault")
    @Mapping(source = "birthday", target = "birthday", qualifiedByName = "localDateToString")
    UserLoginResponse toUserLoginResponse(UserEntity userEntity);

    /**
     * 后处理：确保 UserEntity 存在默认值
     *
     * @param entity UserEntity
     */
    @AfterMapping
    default void setDefaultStatus(@MappingTarget UserEntity entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(UserEmotionStatus.PEACE);
        }
    }
}
