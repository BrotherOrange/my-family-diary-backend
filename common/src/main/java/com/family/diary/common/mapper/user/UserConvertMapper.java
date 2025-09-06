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

package com.family.diary.common.mapper.user;

import com.family.diary.common.enums.status.UserEmotionStatus;
import com.family.diary.common.enums.status.UserFlagStatus;
import com.family.diary.common.exceptions.mapper.MapperException;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserConvertMapper {
    /**
     * UserEmotionStatus -> String
     *
     * @param status UserEmotionStatus
     * @return String
     */
    @Named("userStatusToString")
    default String userStatusToString(UserEmotionStatus status) {
        return status != null ? status.getDescription() : null;
    }

    /**
     * UserEmotionStatus -> String (包含默认值)
     *
     * @param status UserEmotionStatus
     * @return String
     */
    @Named("userStatusToStringWithDefault")
    default String userStatusToStringWithDefault(UserEmotionStatus status) {
        return status != null ? status.getDescription() : UserEmotionStatus.PEACE.getDescription();
    }

    /**
     * String -> UserEmotionStatus
     *
     * @param status Status-String
     * @return String
     * @throws MapperException 映射异常
     */
    @Named("userStringToStatus")
    default UserEmotionStatus userStringToStatus(String status) throws MapperException {
        try {
            return status != null ? UserEmotionStatus.fromDescription(status) : null;
        } catch (IllegalArgumentException e) {
            throw new MapperException("映射字符串不存在：" + e.getMessage());
        }
    }

    /**
     * UserFlagStatus -> String (包含默认值)
     *
     * @param flagStatus UserFlagStatus
     * @return String
     */
    @Named("userFlagToString")
    default String userFlagToString(UserFlagStatus flagStatus) {
        return flagStatus != null ? flagStatus.getDescription() : null;
    }

    /**
     * String -> UserFlagStatus
     *
     * @param flagStatus UserFlagStatus-String
     * @return String
     * @throws MapperException 映射异常
     */
    @Named("userStringToFlag")
    default UserFlagStatus userStringToFlag(String flagStatus) throws MapperException {
        try {
            return flagStatus != null ? UserFlagStatus.valueOf(flagStatus) : null;
        } catch (IllegalArgumentException e) {
            throw new MapperException("映射字符串不存在：" + e.getMessage());
        }
    }
}
