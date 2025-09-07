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

package com.family.diary.infrastructure.dao.community.post;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.family.diary.infrastructure.po.community.post.PostLovePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子与用户收藏关系DAO接口类
 *
 * @author Richard Zhang
 * @since 2025-07-19
 */
@Mapper
public interface PostLoveDAO extends BaseMapper<PostLovePo> {
}
