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
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.infrastructure.po.user.UserPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { CommonConvertMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * UserEntity -> UserPo
     *
     * @param userEntity UserEntity
     * @return UserPo
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserPo toUserPo(UserEntity userEntity);

    /**
     * UserPo -> UserEntity
     *
     * @param userPO UserPo
     * @return UserEntity
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserEntity toUserEntity(UserPo userPO);
}
