package com.gongda.gongdacircle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 帖子数据传输对象
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
public class PostDTO {
    
    /**
     * 帖子标题（可选）
     */
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;
    
    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    @Size(max = 5000, message = "帖子内容不能超过5000个字符")
    private String content;
    
    /**
     * 帖子类型 1-文字 2-图片 3-视频 4-链接
     */
    @NotNull(message = "帖子类型不能为空")
    private Integer type;
    
    /**
     * 话题标签，多个标签用逗号分隔
     */
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;
    
    /**
     * 图片URL，多张图片用逗号分隔
     */
    private String images;
    
    /**
     * 视频URL
     */
    private String video;
    
    /**
     * 链接URL
     */
    private String linkUrl;
    
    /**
     * 链接标题
     */
    private String linkTitle;
    
    /**
     * 链接描述
     */
    private String linkDescription;
    
    /**
     * 位置信息
     */
    private String location;
} 