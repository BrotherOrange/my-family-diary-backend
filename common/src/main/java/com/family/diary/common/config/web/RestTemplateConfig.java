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

package com.family.diary.common.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * RestTemplate配置类
 *
 * @author Richard Zhang
 * @since 2025-07-13
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 默认RestTemplate配置
     *
     * @return RestTemplate
     */
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 获取现有的消息转换器
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        // 找到MappingJackson2HttpMessageConverter并添加text/plain支持
        for (HttpMessageConverter<?> converter : messageConverters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jsonConverter) {
                jsonConverter.setSupportedMediaTypes(Arrays.asList(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN
                ));
                break;
            }
        }

        return restTemplate;
    }
}
