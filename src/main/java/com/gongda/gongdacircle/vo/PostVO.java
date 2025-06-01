package com.gongda.gongdacircle.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子视图对象
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
public class PostVO {
    
    /**
     * 帖子ID
     */
    private Long id;
    
    /**
     * 发布用户ID
     */
    private Long userId;
    
    /**
     * 发布用户信息
     */
    private UserVO userInfo;
    
    /**
     * 帖子标题
     */
    private String title;
    
    /**
     * 帖子内容
     */
    private String content;
    
    /**
     * 帖子类型 1-文字 2-图片 3-视频 4-链接
     */
    private Integer type;
    
    /**
     * 话题标签
     */
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
    
    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;
    
    /**
     * 是否精华 0-否 1-是
     */
    private Integer isEssence;
    
    /**
     * 状态 0-草稿 1-已发布 2-已删除 3-已封禁
     */
    private Integer status;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 收藏数
     */
    private Integer collectCount;
    
    /**
     * 转发数
     */
    private Integer shareCount;
    
    /**
     * 浏览数
     */
    private Integer viewCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 