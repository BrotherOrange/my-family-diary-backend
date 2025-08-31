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

package com.family.diary.common.mapper;

import com.family.diary.common.enums.status.UserEmotionStatus;
import com.family.diary.common.exceptions.mapper.MapperException;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用映射方法
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
@Mapper(componentModel = "spring")
public interface CommonConvertMapper {
    /**
     * UserEmotionStatus -> String
     *
     * @param status UserEmotionStatus
     * @return String
     */
    @Named("statusToString")
    default String statusToString(UserEmotionStatus status) {
        return status != null ? status.getDescription() : null;
    }

    /**
     * UserEmotionStatus -> String (包含默认值)
     *
     * @param status UserEmotionStatus
     * @return String
     */
    @Named("statusToStringWithDefault")
    default String statusToStringWithDefault(UserEmotionStatus status) {
        return status != null ? status.getDescription() : UserEmotionStatus.PEACE.getDescription();
    }

    /**
     * String -> UserEmotionStatus
     *
     * @param status Status-String
     * @return String
     * @throws MapperException 映射异常
     */
    @Named("stringToStatus")
    default UserEmotionStatus stringToStatus(String status) throws MapperException {
        try {
            return status != null ? UserEmotionStatus.fromDescription(status) : null;
        } catch (IllegalArgumentException e) {
            throw new MapperException("映射字符串不存在：" + e.getMessage());
        }
    }

    /**
     * LocalDateTime -> String
     *
     * @param dateTime LocalDateTime
     * @return DateTime-String
     */
    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * String -> LocalDateTime
     *
     * @param dateTimeStr DateTime-String
     * @return LocalDateTime
     */
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * LocalDate -> String
     *
     * @param date LocalDate
     * @return LocalDate-String
     */
    @Named("localDateToString")
    default String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * String -> LocalDate
     *
     * @param dateStr LocalDate-String
     * @return LocalDate
     */
    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
