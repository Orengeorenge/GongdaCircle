-- 创建数据库
CREATE DATABASE IF NOT EXISTS `gongda_circle` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `gongda_circle`;

-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint DEFAULT '2' COMMENT '性别 0-女 1-男 2-保密',
  `birthday` varchar(20) DEFAULT NULL COMMENT '生日',
  `biography` varchar(500) DEFAULT NULL COMMENT '个人简介',
  `school` varchar(100) DEFAULT NULL COMMENT '学校',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `status` tinyint DEFAULT '1' COMMENT '状态 0-禁用 1-正常',
  `last_login_time` varchar(30) DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 帖子表
CREATE TABLE `post` (
  `id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '发布用户ID',
  `title` varchar(200) DEFAULT NULL COMMENT '帖子标题',
  `content` text NOT NULL COMMENT '帖子内容',
  `type` tinyint DEFAULT '1' COMMENT '帖子类型 1-文字 2-图片 3-视频 4-链接',
  `tags` varchar(500) DEFAULT NULL COMMENT '话题标签',
  `images` text DEFAULT NULL COMMENT '图片URL，多张图片用逗号分隔',
  `video` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `link_url` varchar(255) DEFAULT NULL COMMENT '链接URL',
  `link_title` varchar(200) DEFAULT NULL COMMENT '链接标题',
  `link_description` varchar(500) DEFAULT NULL COMMENT '链接描述',
  `location` varchar(200) DEFAULT NULL COMMENT '位置信息',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶 0-否 1-是',
  `is_essence` tinyint DEFAULT '0' COMMENT '是否精华 0-否 1-是',
  `status` tinyint DEFAULT '1' COMMENT '状态 0-草稿 1-已发布 2-已删除 3-已封禁',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `collect_count` int DEFAULT '0' COMMENT '收藏数',
  `share_count` int DEFAULT '0' COMMENT '转发数',
  `view_count` int DEFAULT '0' COMMENT '浏览数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_is_essence` (`is_essence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- 好友关系表
CREATE TABLE `friend` (
  `id` bigint NOT NULL COMMENT '关系ID',
  `user_id` bigint NOT NULL COMMENT '申请者用户ID',
  `friend_id` bigint NOT NULL COMMENT '被申请者用户ID',
  `status` tinyint DEFAULT '0' COMMENT '状态 0-待确认 1-已通过 2-已拒绝 3-已删除',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注信息',
  `reason` varchar(500) DEFAULT NULL COMMENT '申请理由',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_friend_id` (`friend_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友关系表';

-- 评论表
CREATE TABLE `comment` (
  `id` bigint NOT NULL COMMENT '评论ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父评论ID',
  `reply_user_id` bigint DEFAULT NULL COMMENT '回复用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `images` text DEFAULT NULL COMMENT '图片URL，多张图片用逗号分隔',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `status` tinyint DEFAULT '1' COMMENT '状态 0-待审核 1-已发布 2-已删除 3-已封禁',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 帖子点赞表
CREATE TABLE `post_like` (
  `id` bigint NOT NULL COMMENT '点赞ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`, `deleted`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子点赞表';

-- 帖子收藏表
CREATE TABLE `post_collect` (
  `id` bigint NOT NULL COMMENT '收藏ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`, `deleted`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子收藏表';

-- 系统通知表
CREATE TABLE `system_notice` (
  `id` bigint NOT NULL COMMENT '通知ID',
  `title` varchar(200) NOT NULL COMMENT '通知标题',
  `content` text NOT NULL COMMENT '通知内容',
  `type` tinyint DEFAULT '1' COMMENT '通知类型 1-系统通知 2-活动通知 3-更新通知',
  `target_type` tinyint DEFAULT '1' COMMENT '目标类型 1-全部用户 2-指定用户',
  `target_ids` text DEFAULT NULL COMMENT '目标用户ID，多个用逗号分隔',
  `status` tinyint DEFAULT '1' COMMENT '状态 0-草稿 1-已发布 2-已撤回',
  `publish_time` varchar(30) DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统通知表';

-- 插入测试数据
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `gender`, `school`, `major`, `grade`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqNhKwCFqxEwmHcUmfgxFvjxK', '管理员', 'admin@gongda.edu.cn', '13800138000', 2, '工业大学', '计算机科学与技术', '2024', 1),
(2, 'student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqNhKwCFqxEwmHcUmfgxFvjxK', '学生一号', 'student1@gongda.edu.cn', '13800138001', 1, '工业大学', '软件工程', '2023', 1),
(3, 'student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVqNhKwCFqxEwmHcUmfgxFvjxK', '学生二号', 'student2@gongda.edu.cn', '13800138002', 0, '工业大学', '信息安全', '2022', 1);

INSERT INTO `post` (`id`, `user_id`, `title`, `content`, `type`, `tags`, `status`, `like_count`, `comment_count`, `view_count`) VALUES
(1, 1, '欢迎来到工大圈！', '这里是工业大学学生的专属社交平台，大家可以在这里分享学习、生活、娱乐等各种内容。', 1, '欢迎,工大圈,社交', 1, 10, 5, 100),
(2, 2, '今天的学习心得', '今天学习了Spring Boot框架，感觉收获很大！分享一些学习笔记给大家。', 1, '学习,Spring Boot,编程', 1, 8, 3, 80),
(3, 3, '校园美景分享', '今天天气很好，拍了一些校园的美景照片，和大家分享一下。', 2, '校园,美景,摄影', 1, 15, 7, 120);

INSERT INTO `system_notice` (`id`, `title`, `content`, `type`, `target_type`, `status`, `publish_time`) VALUES
(1, '工大圈正式上线！', '欢迎大家使用工大圈社交平台，这里是属于工业大学学生的专属空间。', 1, 1, 1, '2024-01-01 00:00:00'),
(2, '平台使用规范', '为了营造良好的社区环境，请大家遵守平台使用规范，文明发言。', 1, 1, 1, '2024-01-01 12:00:00'); 