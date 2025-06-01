package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.entity.Comment;
import com.gongda.gongdacircle.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 分页查询帖子评论列表（包含用户信息）
     */
    Page<CommentVO> selectCommentPage(Page<CommentVO> page, @Param("postId") Long postId, @Param("currentUserId") Long currentUserId);
    
    /**
     * 查询指定评论的回复列表
     */
    List<CommentVO> selectRepliesByParentId(@Param("parentId") Long parentId, @Param("currentUserId") Long currentUserId);
    
    /**
     * 统计帖子评论数
     */
    int countByPostId(@Param("postId") Long postId);
} 