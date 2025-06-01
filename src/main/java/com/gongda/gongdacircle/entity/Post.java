package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("post")
public class Post extends BaseEntity {
    
    private Long userId;
    private String title;
    private String content;
    private Integer type;
    private String tags;
    private String images;
    private String video;
    private String linkUrl;
    private String linkTitle;
    private String linkDescription;
    private String location;
    private Integer isTop;
    private Integer isEssence;
    private Integer status;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer shareCount;
    private Integer viewCount;
} 