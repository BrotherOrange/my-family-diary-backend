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

import com.family.diary.common.enums.status.UserEmotionStatus;
import com.family.diary.domain.entity.user.UserEntity;
import com.family.diary.infrastructure.po.user.UserPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    UserPo toUserPo(UserEntity userEntity);

    @Mapping(source = "status", target = "status", qualifiedByName = "stringToStatus")
    UserEntity toUserEntity(UserPo userPO);

    @Named("statusToString")
    default String statusToString(UserEmotionStatus status) {
        return status != null ? status.getDescription() : null; // 假设你的枚举有 getCode() 方法
    }

    @Named("stringToStatus")
    default UserEmotionStatus stringToStatus(String status) {
        return status != null ? UserEmotionStatus.valueOf(status) : null; // 假设你有 fromCode 方法
    }
}
