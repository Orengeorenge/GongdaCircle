package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongda.gongdacircle.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 帖子Mapper接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
    /**
     * 增加帖子浏览量
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET view_count = view_count + 1 WHERE id = #{id} AND deleted = 0")
    int increaseViewCount(@Param("id") Long id);
    
    /**
     * 增加帖子点赞数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET like_count = like_count + 1 WHERE id = #{id} AND deleted = 0")
    int increaseLikeCount(@Param("id") Long id);
    
    /**
     * 减少帖子点赞数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0 AND deleted = 0")
    int decreaseLikeCount(@Param("id") Long id);
    
    /**
     * 增加帖子收藏数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET collect_count = collect_count + 1 WHERE id = #{id} AND deleted = 0")
    int increaseCollectCount(@Param("id") Long id);
    
    /**
     * 减少帖子收藏数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET collect_count = collect_count - 1 WHERE id = #{id} AND collect_count > 0 AND deleted = 0")
    int decreaseCollectCount(@Param("id") Long id);
    
    /**
     * 增加帖子评论数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET comment_count = comment_count + 1 WHERE id = #{id} AND deleted = 0")
    int increaseCommentCount(@Param("id") Long id);
    
    /**
     * 减少帖子评论数
     * @param id 帖子ID
     * @return 影响行数
     */
    @Update("UPDATE post SET comment_count = comment_count - 1 WHERE id = #{id} AND comment_count > 0 AND deleted = 0")
    int decreaseCommentCount(@Param("id") Long id);
} 