package com.gongda.gongdacircle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 评论数据传输对象
 */
@Data
public class CommentDTO {
    
    /**
     * 帖子ID
     */
    @NotNull(message = "帖子ID不能为空")
    private Long postId;
    
    /**
     * 父评论ID（可选，用于回复评论）
     */
    private Long parentId;
    
    /**
     * 回复用户ID（可选，用于回复指定用户）
     */
    private Long replyUserId;
    
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容不能超过1000个字符")
    private String content;
    
    /**
     * 图片URL，多张图片用逗号分隔
     */
    @Size(max = 2000, message = "图片URL总长度不能超过2000个字符")
    private String images;
} 