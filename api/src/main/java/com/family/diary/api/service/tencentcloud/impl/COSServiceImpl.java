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

package com.family.diary.api.service.tencentcloud.impl;

import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.common.config.tencentcloud.COSConfig;
import com.family.diary.common.constants.common.ImageConstants;
import com.family.diary.common.constants.tencentcloud.COSConstants;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.common.ImageUtils;
import com.family.diary.common.utils.redis.RedisUtil;
import com.family.diary.common.utils.tencentcloud.COSUtil;
import com.family.diary.domain.entity.tencentcloud.cos.COSAvatarUploadEntity;
import com.qcloud.cos.COSClient;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * COS对象服务Service实现类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class COSServiceImpl implements COSService {
    private final COSUtil cosUtil;

    private final COSConfig cosConfig;

    private final RedisUtil redisUtil;

    private final ImageUtils imageUtils;

    @Resource
    private COSClient cosClientWithTempInfo;

    @Value("${tencent-cloud.cos.bucket}")
    private String bucket;

    @Override
    public String uploadAvatarToCOS(COSAvatarUploadEntity entity) throws BaseException {
        cosClientWithTempInfo = cosConfig.cosClientWithTempInfo();
        var openid = entity.getOpenId();
        var base64Image = entity.getBase64Image();
        var fileFormat = imageUtils.getContentTypeFromBase64(base64Image)
                .replace(ImageConstants.IMAGE_PREFIX, Strings.EMPTY);
        var filePath = buildFilePathWithId(openid, COSConstants.AVATARS_DIR, fileFormat);
        var imageUrl = imageUtils.uploadBase64ImageToCOS(cosClientWithTempInfo, base64Image, filePath);
        if (imageUrl == null || imageUrl.isEmpty()) {
            log.error("上传头像到 COS 失败，文件存储路径：{}", filePath);
            return "";
        }
        saveAvatarCache(openid, imageUrl);
        log.info("上传图片到 COS 成功，文件存储路径：{}，临时访问地址：{}", filePath, imageUrl);
        return imageUrl;
    }

    @Override
    public String getAvatarUrl(String openid) {
        log.info("试图获取用户头像的临时链接，openid:{}", openid);
        var cacheKey = getAvatarCacheKey(openid);
        // 使用 RedisUtil 从缓存中获取
        var cachedUrl = (String) redisUtil.get(cacheKey);
        if (cachedUrl != null && !cachedUrl.isEmpty()) {
            log.info("从 Redis 缓存中获取到用户头像链接，openid:{}", openid);
            return cachedUrl;
        }
        // 缓存未命中，生成新链接并缓存
        log.info("Redis 缓存未命中，生成新的头像链接，openid:{}", openid);
        var filePath = buildFilePathWithId(openid, COSConstants.AVATARS_DIR, ImageConstants.IMAGE_PNG_FORMAT);
        var avatarUrl = cosUtil.generatePresignedUrlWithOutHost(cosClientWithTempInfo, bucket, filePath,
                ImageConstants.MAX_VALID_TIME);
        if (!avatarUrl.isBlank()) {
            saveAvatarCache(openid, avatarUrl);
        }
        return avatarUrl;
    }

    private String buildFilePathWithId(String id, String dir, String fileFormat) {
        return String.format("%s/%s.%s", dir, id, fileFormat);
    }

    private void saveAvatarCache(String openid, String avatarUrl) {
        var cacheKey = getAvatarCacheKey(openid);
        var success = redisUtil.setWithExpire(cacheKey, avatarUrl, 3600, TimeUnit.SECONDS);
        if (!success) {
            log.warn("Redis 缓存头像链接失败，openid: {}", openid);
        }
    }

    private String getAvatarCacheKey(String openid) {
        return String.format("%s:%s", COSConstants.AVATARS_CACHE_KEY_PREFIX, openid);
    }
}
