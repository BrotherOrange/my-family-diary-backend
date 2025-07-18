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

package com.family.diary.common.config.tencentcloud;

import com.family.diary.common.constants.tencentcloud.COSConstants;
import com.family.diary.common.exceptions.tencentcloud.InvalidCOSTempInfoException;
import com.family.diary.common.models.tencentcloud.COSTempInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.Jackson;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Policy;
import com.tencent.cloud.Statement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TreeMap;

/**
 * 腾讯云COS服务客户端配置类
 *
 * @author Richard Zhang
 * @since 2025-07-14
 */
@Configuration
public class COSConfig {
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
     * 默认永久token的COSClient
     *
     * @return COSClient
     */
    @Bean(name = "cosClient")
    public COSClient cosClient() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(apiSecretId, apiSecretKey);
        // 2 设置 bucket 的地域
        Region region = new Region(cosRegion);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端
        return new COSClient(cred, clientConfig);
    }

    /**
     * 使用临时token的COSClient
     *
     * @return COSClient
     */
    public COSClient cosClientWithTempInfo() {
        COSTempInfo cosTempInfo = getCosTempInfo();
        COSCredentials cred = new BasicSessionCredentials(cosTempInfo.cosTempSecretId(), cosTempInfo.cosTempSecretKey(),
                cosTempInfo.cosTempToken());
        // 2 设置 bucket 的地域
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端
        return new COSClient(cred, clientConfig);
    }

    private COSTempInfo getCosTempInfo() {
        TreeMap<String, Object> config = new TreeMap<>();
        try {
            // 替换为您的云 api 密钥 SecretId
            config.put("secretId", apiSecretId);
            config.put("secretKey", apiSecretKey);

            // 初始化 policy
            var policy = new Policy();
            config.put("durationSeconds", COSConstants.TEMP_TOKEN_EXPIRE_TIME);

            config.put("bucket", bucket);
            config.put("region", cosRegion);

            // 开始构建一条 statement
            var statement = new Statement();

            statement.setEffect("allow");
            statement.addActions(new String[] {
                    "cos:*"
            });

            statement.addResources(new String[] {
                    String.format("qcs::cos:%s:uid/%s:%s/*", cosRegion, appId, bucket),
                    String.format("qcs::ci:%s:uid/%s:bucket/%s/*", cosRegion, appId, bucket)
            });

            // 把 statement 添加到 policy
            policy.addStatement(statement);
            // 将 Policy 转化成 String
            config.put("policy", Jackson.toJsonPrettyString(policy));

            var response = CosStsClient.getCredential(config);
            return new COSTempInfo(response.credentials.tmpSecretId, response.credentials.tmpSecretKey,
                    response.credentials.sessionToken);
        } catch (Exception e) {
            throw new InvalidCOSTempInfoException("无法获取到COS服务临时信息！");
        }
    }
}
