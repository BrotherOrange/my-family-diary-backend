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

import com.family.diary.common.factory.COSClientFactory;
import com.qcloud.cos.COSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS服务配置类
 * 负责定义COS相关的Spring Bean
 *
 * @author Richard Zhang
 * @since 2025-07-14
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class COSConfig {
    private final COSClientFactory cosClientFactory;

    /**
     * 永久密钥的COSClient Bean（共享实例）
     * 注意：此Bean不应在使用后被shutdown，适用于长期持有的场景
     *
     * @return COSClient
     */
    @Bean(name = "cosClient")
    public COSClient cosClient() {
        return cosClientFactory.createPermanentClient();
    }

    /**
     * 临时密钥的COSClient Bean（共享实例）
     * 注意：临时凭证有效期为2小时，适用于需要临时权限的场景
     *
     * @return COSClient
     */
    @Bean(name = "cosClientWithTempInfo")
    public COSClient cosClientWithTempInfo() {
        return cosClientFactory.createTemporaryClient();
    }
}
