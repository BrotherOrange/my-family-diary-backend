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

package com.family.diary.common.models.tencentcloud;

/**
 * COSClient需要的临时信息
 *
 * @param cosTempSecretId cosTempSecretId
 * @param cosTempSecretKey cosTempSecretKey
 * @param cosTempToken cosTempToken
 */
public record COSTempInfo(String cosTempSecretId,
                          String cosTempSecretKey,
                          String cosTempToken) {
}
