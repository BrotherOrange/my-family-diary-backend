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

package com.family.diary.common.utils.common;

import com.family.diary.common.constants.tencentcloud.COSConstants;
import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.tencentcloud.COSUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * 工具类：处理图片相关操作
 *
 * @author Richard Zhang
 * @since 2025-07-13
 */
@Slf4j
@Component
public class ImageUtils {
    @Resource
    private COSUtil cosUtil;

    @Value("${tencent-cloud.cos.bucket}")
    private String bucket;

    /**
     * 将 Base64 编码的图片上传到腾讯云 COS
     *
     * @param cosClientWithTempInfo 已经配置好临时密钥的 COS 客户端
     * @param base64Image           包含前缀的 Base64 编码字符串
     * @param cosKey                上传到 COS 后的文件名称（例如：image/avatar.png）
     * @return 文件在 COS 中的 URL
     * @throws BaseException 如果上传失败，抛出自定义异常
     */
    public String uploadBase64ImageToCOS(COSClient cosClientWithTempInfo, String base64Image, String cosKey)
            throws BaseException {
        // 将 Base64 转换为 byte 数组
        byte[] imageBytes = convertBase64ToImageBytes(base64Image);

        // 2. 构造输入流
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        // 3. 创建 ObjectMetadata 并设置内容类型（根据 Base64 前缀推断）
        ObjectMetadata metadata = new ObjectMetadata();
        String contentType = getContentTypeFromBase64(base64Image);
        metadata.setContentType(contentType); // 自动识别图片类型
        metadata.setContentLength(imageBytes.length); // 设置精确长度（避免使用 inputStream.available()）

        // 4. 构造 PutObjectRequest（支持 InputStream）
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                cosKey,
                inputStream,
                metadata
        );

        // 5. 上传到 COS
        try {
            PutObjectResult result = cosClientWithTempInfo.putObject(putObjectRequest);
            if (result != null) {
                // 上传成功后返回文件 URL
                return cosUtil.generatePresignedUrlWithOutHost(cosClientWithTempInfo, bucket, cosKey,
                        COSConstants.TEMP_TOKEN_EXPIRE_TIME);
            }
        } catch (Exception e) {
            throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "上传图片到 COS 失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.warn("关闭输入流时发生异常: {}", e.getMessage());
            }
        }

        return Strings.EMPTY; // 如果上传失败，返回 null
    }

    /**
     * 将Base64编码的图片转换为本地文件
     *
     * @param base64Image 包含前缀的Base64编码字符串
     * @param outputPath  输出文件的路径
     * @throws BaseException 如果转换或写入文件失败，抛出自定义异常
     */
    public void convertBase64ToImageFile(String base64Image, String outputPath) throws BaseException {
        byte[] imageBytes = convertBase64ToImageBytes(base64Image);
        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(imageBytes);
        } catch (IOException ioe) {
            throw new BaseException(ExceptionErrorCode.COMMON_ERROR, "无法写入图片文件", ioe);
        }
    }

    /**
     * 将Base64编码的图片转换byte数组
     *
     * @param base64Image 包含前缀的Base64编码字符串
     * @return byte数组，表示解码后的图片数据
     */
    public byte[] convertBase64ToImageBytes(String base64Image) {
        // 提取Base64数据部分，去除前缀
        String base64Data = extractBase64Data(base64Image);

        // 解码Base64数据
        return Base64.getDecoder().decode(base64Data);
    }

    /**
     * 根据 Base64 前缀推断内容类型（Content-Type）
     * 例如：data:image/png;base64 -> image/png
     */
    public String getContentTypeFromBase64(String base64Image) {
        if (base64Image.startsWith("data:image/png;base64")) {
            return "image/png";
        } else if (base64Image.startsWith("data:image/jpeg;base64")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("data:image/jpg;base64")) {
            return "image/jpg";
        } else if (base64Image.startsWith("data:image/gif;base64")) {
            return "image/gif";
        } else {
            return "application/octet-stream"; // 默认类型
        }
    }

    /**
     * 从Base64字符串中提取数据部分（去除前缀）
     *
     * @param base64String 包含前缀的Base64字符串
     * @return 纯Base64编码的数据
     */
    public String extractBase64Data(String base64String) {
        int commaIndex = base64String.indexOf(',');
        if (commaIndex != -1 && commaIndex < base64String.length() - 1) {
            return base64String.substring(commaIndex + 1);
        }
        return base64String; // 如果没有找到逗号，就返回原字符串
    }
}