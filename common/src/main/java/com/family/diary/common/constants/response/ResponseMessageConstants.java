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

package com.family.diary.common.constants.response;

/**
 * 响应消息常量类
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
public interface ResponseMessageConstants {

    // ==================== 通用消息 ====================
    String SUCCESS = "操作成功";
    String FAILED = "操作失败";

    // ==================== 认证相关 ====================
    String UNAUTHORIZED = "未授权或Token已过期";
    String ACCESS_TOKEN_EXPIRED = "Access Token已过期";
    String REFRESH_TOKEN_EXPIRED = "Refresh Token已过期，请重新登录";
    String REFRESH_TOKEN_INVALID = "Refresh Token无效";
    String USER_NOT_LOGGED_IN = "用户未登录";
    String LOGIN_FAILED = "登录失败";
    String LOGOUT_SUCCESS = "登出成功";

    // ==================== 请求相关 ====================
    String MISSING_TRACE_ID = "Missing or empty trace ID";
    String INVALID_REQUEST = "非法请求";
}
