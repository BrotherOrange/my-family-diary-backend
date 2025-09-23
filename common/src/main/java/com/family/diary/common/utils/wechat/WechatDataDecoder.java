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

package com.family.diary.common.utils.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 微信小程序信息解码工具类
 *
 * @author Richard Zhang
 * @since 2025-07-15
 */
@Slf4j
@Component
public class WechatDataDecoder {
    /**
     * 解密微信小程序加密数据
     *
     * @param encryptedData encryptedData
     * @param sessionKey sessionKey
     * @param iv iv
     * @return String 解密后的字符串
     */
    public String decryptData(String encryptedData, String sessionKey, String iv) throws Exception {
        // 解码 Base64 字符串
        log.debug("Encrypted Data: {}", encryptedData);
        log.debug("Session Key: {}", sessionKey);
        log.debug("IV: {}", iv);
        var encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        var sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        var ivBytes = Base64.getDecoder().decode(iv);

        // 创建 AES 密钥和初始向量
        log.debug("准备解密Session Key 和 IV");
        var secretKeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        var ivParameterSpec = new IvParameterSpec(ivBytes);

        // 初始化 Cipher
        log.debug("初始化 AES/CBC/PKCS5Padding 解密模式");
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 解密
        log.debug("开始解密数据");
        var decryptedBytes = cipher.doFinal(encryptedDataBytes);
        log.debug("解密完成");
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
