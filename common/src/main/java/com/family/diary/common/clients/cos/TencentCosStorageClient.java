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

package com.family.diary.common.clients.cos;

import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.factory.tencentcloud.COSClientFactory;
import com.family.diary.common.utils.common.ImageUtils;
import com.family.diary.common.utils.tencentcloud.COSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 腾讯云COS客户端实现
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TencentCosStorageClient implements CosStorageClient {

    private final COSUtil cosUtil;

    private final COSClientFactory cosClientFactory;

    private final ImageUtils imageUtils;

    @Value("${tencent-cloud.cos.bucket}")
    private String bucket;

    @Value("${tencent-cloud.cos.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${tencent-cloud.cos.retry.base-backoff-ms:200}")
    private long baseBackoffMs;

    @Override
    public String uploadBase64Image(String base64Image, String objectKey) {
        return withRetry("COS上传", () -> {
            var tempClient = cosClientFactory.createTemporaryClient();
            var url = imageUtils.uploadBase64ImageToCOS(tempClient, base64Image, objectKey);
            if (url == null || url.isBlank()) {
                throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "上传图片到 COS 失败");
            }
            return url;
        });
    }

    @Override
    public String generatePresignedUrl(String objectKey, long expirationInSeconds) {
        return withRetry("COS生成预签名URL", () -> {
            var permanentClient = cosClientFactory.createPermanentClient();
            var url = cosUtil.generatePresignedUrlWithOutHost(permanentClient, bucket, objectKey, expirationInSeconds);
            if (url == null || url.isBlank()) {
                throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "生成预签名URL失败");
            }
            return url;
        });
    }

    private <T> T withRetry(String action, Supplier<T> supplier) {
        RuntimeException lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
                log.warn("{}失败，准备重试 {}/{}: {}", action, attempt, maxAttempts, e.getMessage());
                if (attempt < maxAttempts) {
                    sleepSilently(baseBackoffMs * attempt);
                }
            }
        }
        throw lastException != null ? lastException : new RuntimeException(action + "失败");
    }

    private void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
