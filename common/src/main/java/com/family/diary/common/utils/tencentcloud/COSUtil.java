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

package com.family.diary.common.utils.tencentcloud;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ResponseHeaderOverrides;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;

import static com.qcloud.cos.http.HttpMethodName.GET;

/**
 * 腾讯云对象存储COS服务——工具类
 *
 * @author Richard Zhang
 * @since 2025-07-14
 */
@Slf4j
@Component
public class COSUtil {
    @Value("${tencent-cloud.cos.host}")
    private String host;

    @Value("${tencent-cloud.cos.default-host}")
    private String defaultHost;

    /**
     * 生成COS对象的预签名URL（临时链接）
     *
     * @param objectKey           对象在COS中的路径
     * @param expirationInSeconds 链接过期时间（秒）
     * @return 预签名URL
     */
    public String generatePresignedUrlWithOutHost(COSClient cosClient, String bucketName, String objectKey,
                                                  long expirationInSeconds) {
        try {
            // 设置签名URL有效时间
            Date expiration = new Date(System.currentTimeMillis() + expirationInSeconds * 1000);

            // 生成预签名URL请求
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectKey);
            // 设置签名过期时间
            ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
            req.setResponseHeaders(responseHeaders);
            req.setExpiration(expiration);
            req.setMethod(GET);

            // 获取预签名URL
            URL url = cosClient.generatePresignedUrl(req, false);
            String finalUrl = url.toString()
                    .replaceAll(Matcher.quoteReplacement(defaultHost), Matcher.quoteReplacement(host));
            log.info("Generated presigned URL: {}", finalUrl);
            return finalUrl;
        } catch (Exception e) {
            log.error("Generate presigned url error", e);
            return Strings.EMPTY;
        } finally {
            // 关闭客户端
            cosClient.shutdown();
        }
    }
}
