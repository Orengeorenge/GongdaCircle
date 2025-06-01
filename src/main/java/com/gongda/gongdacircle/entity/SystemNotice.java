package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_notice")
public class SystemNotice extends BaseEntity {
    
    private String title;
    private String content;
    private Integer type;
    private Integer targetType;
    private String targetIds;
    private Integer status;
    private String publishTime;
} 