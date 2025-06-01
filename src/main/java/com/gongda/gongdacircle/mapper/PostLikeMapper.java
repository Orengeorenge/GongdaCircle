package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongda.gongdacircle.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子点赞Mapper接口
 */
@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    
    /**
     * 检查用户是否已点赞帖子
     */
    int checkUserLiked(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 统计帖子点赞数
     */
    int countByPostId(@Param("postId") Long postId);
} 