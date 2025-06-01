package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongda.gongdacircle.entity.PostCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子收藏Mapper接口
 */
@Mapper
public interface PostCollectMapper extends BaseMapper<PostCollect> {
    
    /**
     * 检查用户是否已收藏帖子
     */
    int checkUserCollected(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 统计帖子收藏数
     */
    int countByPostId(@Param("postId") Long postId);
} 