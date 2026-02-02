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

package com.family.diary.common.factory;

import com.family.diary.common.constants.tencentcloud.COSConstants;
import com.family.diary.common.exceptions.tencentcloud.InvalidCOSTempInfoException;
import com.family.diary.common.models.tencentcloud.COSTempInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.Jackson;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Policy;
import com.tencent.cloud.Statement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

/**
 * 腾讯云COS客户端工厂类
 * 负责创建永久密钥或临时密钥的COSClient实例
 *
 * @author Richard Zhang
 * @since 2025-07-14
 */
@Component
public class COSClientFactory {
    @Value("${tencent-cloud.app-id}")
    private String appId;

    @Value("${tencent-cloud.api-secret-id}")
    private String apiSecretId;

    @Value("${tencent-cloud.api-secret-key}")
    private String apiSecretKey;

    @Value("${tencent-cloud.cos.region}")
    private String cosRegion;

    @Value("${tencent-cloud.cos.bucket}")
    private String bucket;

    /**
     * 创建永久密钥的COSClient实例
     * 适用于：生成预签名URL等需要长期有效的场景
     *
     * @return COSClient 新的客户端实例
     */
    public COSClient createPermanentClient() {
        var cred = new BasicCOSCredentials(apiSecretId, apiSecretKey);
        var region = new Region(cosRegion);
        var clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    /**
     * 创建临时密钥的COSClient实例
     * 适用于：文件上传等需要即时执行的操作
     *
     * @return COSClient 新的客户端实例（带临时凭证）
     */
    public COSClient createTemporaryClient() {
        var cosTempInfo = getCosTempInfo();
        var cred = new BasicSessionCredentials(
                cosTempInfo.cosTempSecretId(),
                cosTempInfo.cosTempSecretKey(),
                cosTempInfo.cosTempToken()
        );
        var region = new Region(cosRegion);
        var clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    /**
     * 获取临时凭证信息
     */
    private COSTempInfo getCosTempInfo() {
        var config = new TreeMap<String, Object>();
        try {
            config.put("secretId", apiSecretId);
            config.put("secretKey", apiSecretKey);
            config.put("durationSeconds", COSConstants.TEMP_TOKEN_EXPIRE_TIME);
            config.put("bucket", bucket);
            config.put("region", cosRegion);

            var policy = new Policy();
            var statement = new Statement();
            statement.setEffect("allow");
            statement.addActions(new String[]{"cos:*"});
            statement.addResources(new String[]{
                    String.format("qcs::cos:%s:uid/%s:%s/*", cosRegion, appId, bucket),
                    String.format("qcs::ci:%s:uid/%s:bucket/%s/*", cosRegion, appId, bucket)
            });
            policy.addStatement(statement);
            config.put("policy", Jackson.toJsonPrettyString(policy));

            var response = CosStsClient.getCredential(config);
            return new COSTempInfo(
                    response.credentials.tmpSecretId,
                    response.credentials.tmpSecretKey,
                    response.credentials.sessionToken
            );
        } catch (Exception e) {
            throw new InvalidCOSTempInfoException("无法获取到COS服务临时信息！");
        }
    }
}
