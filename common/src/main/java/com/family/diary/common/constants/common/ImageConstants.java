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

package com.family.diary.common.constants.common;

/**
 * 图片相关常量类
 *
 * @author Richard Zhang
 * @since 2025-11-22
 */
public interface ImageConstants {
    String  IMAGE_PREFIX      = "image/";

    String  IMAGE_JPEG        = "image/jpeg";
    String  IMAGE_JPEG_FORMAT = "jpeg";
    String  IMAGE_PNG         = "image/png";
    String  IMAGE_PNG_FORMAT  = "png";
    String  IMAGE_GIF         = "image/gif";
    String  IMAGE_GIF_FORMAT  = "gif";
    String  IMAGE_BMP         = "image/bmp";
    String  IMAGE_BMP_FORMAT  = "bmp";
    String  IMAGE_WEBP        = "image/webp";
    String  IMAGE_WEBP_FORMAT = "webp";

    Integer MAX_IMAGE_SIZE_MB = 5;
    Integer MAX_IMAGE_WIDTH   = 1920;
    Integer MAX_IMAGE_HEIGHT  = 1080;

    Integer MAX_VALID_TIME = 24 * 60 * 60;
}
