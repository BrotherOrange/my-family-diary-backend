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

import com.family.diary.api.dto.request.tencentcloud.cos.COSAvatarUploadRequest;
import com.family.diary.api.service.tencentcloud.COSService;
import com.family.diary.common.config.common.ImageConstants;
import com.family.diary.common.constants.tencentcloud.COSConstants;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.utils.common.ImageUtils;
import com.family.diary.common.utils.tencentcloud.COSUtil;
import com.qcloud.cos.COSClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class COSServiceImpl implements COSService {
    @Resource
    private COSClient cosClientWithTempInfo;

    @Resource
    private COSUtil cosUtil;

    @Resource
    private ImageUtils imageUtils;

    @Value("${tencent-cloud.cos.bucket}")
    private String bucket;

    @Override
    public String uploadAvatarToCOS(COSAvatarUploadRequest request) throws BaseException {
        String openid = request.getOpenId();
        String base64Image = request.getBase64Image();
        String fileFormat = imageUtils.getContentTypeFromBase64(base64Image).replace(ImageConstants.IMAGE_PREFIX, Strings.EMPTY);
        String filePath = buildFilePathWithId(openid, COSConstants.AVATARS_DIR, fileFormat);
        String imageUrl = imageUtils.uploadBase64ImageToCOS(cosClientWithTempInfo, base64Image, filePath);
        if (imageUrl == null || imageUrl.isEmpty()) {
            log.error("上传头像到 COS 失败，文件存储路径：{}", filePath);
            return "";
        }
        log.info("上传图片到 COS 成功，文件存储路径：{}，临时访问地址：{}", filePath, imageUrl);
        return imageUrl;
    }

    @Override
    public String getAvatarUrl(String openid) {
        log.info("试图获取用户头像的临时链接，openid:{}", openid);
        String filePath = buildFilePathWithId(openid, COSConstants.AVATARS_DIR, ImageConstants.IMAGE_PNG_FORMAT);
        return cosUtil.generatePresignedUrlWithOutHost(cosClientWithTempInfo, bucket, filePath,
                ImageConstants.MAX_VALID_TIME);
    }

    private String buildFilePathWithId(String id, String dir, String fileFormat) {
        return String.format("%s/%s.%s", dir, id, fileFormat);
    }
}
