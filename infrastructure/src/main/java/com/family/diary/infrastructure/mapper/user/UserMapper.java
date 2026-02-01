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

package com.family.diary.infrastructure.mapper.user;

import com.family.diary.common.mapper.CommonConvertMapper;
import com.family.diary.common.mapper.user.UserConvertMapper;
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.infrastructure.po.user.UserPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 用户持久化与实体映射接口类
 *
 * @author Richard Zhang
 * @since 2025-07-19
 */
@Mapper(componentModel = "spring", uses = { CommonConvertMapper.class, UserConvertMapper.class })
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * UserEntity -> UserPo
     *
     * @param userEntity UserEntity
     * @return UserPo
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "userStatusToString")
    @Mapping(target = "flag", source = "flag", qualifiedByName = "userFlagToString")
    UserPo toUserPo(UserEntity userEntity);

    /**
     * UserPo -> UserEntity
     *
     * @param userPO UserPo
     * @return UserEntity
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "userStringToStatus")
    @Mapping(target = "flag", source = "flag", qualifiedByName = "userStringToFlag")
    UserEntity toUserEntity(UserPo userPO);
}
