# GongdaCircle - 工大圈社交平台

## 项目简介
工大圈是一个基于Spring Boot的社交平台后端服务，为大学生提供交流、分享、互动的平台。

## 技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.3.1
- **连接池**: Druid 1.2.16
- **缓存**: Redis
- **安全**: Spring Security
- **工具库**: Hutool 5.8.16
- **JSON**: FastJSON 2.0.25
- **JWT**: JJWT 0.9.1

## 项目结构
```
src/
├── main/
│   ├── java/com/gongda/gongdacircle/
│   │   ├── GongdaCircleApplication.java     # 启动类
│   │   ├── common/                          # 公共类
│   │   │   ├── BaseEntity.java             # 基础实体类
│   │   │   └── Result.java                 # 统一响应结果
│   │   ├── controller/                      # 控制器层
│   │   │   ├── UserController.java         # 用户控制器
│   │   │   ├── PostController.java         # 帖子控制器
│   │   │   ├── FriendController.java       # 好友控制器
│   │   │   ├── CommentController.java      # 评论控制器
│   │   │   └── SystemController.java       # 系统控制器
│   │   ├── dto/                            # 数据传输对象
│   │   │   ├── UserDTO.java               # 用户DTO
│   │   │   ├── PostDTO.java               # 帖子DTO
│   │   │   ├── FriendDTO.java             # 好友DTO
│   │   │   ├── CommentDTO.java            # 评论DTO
│   │   │   └── SystemNoticeDTO.java       # 系统通知DTO
│   │   ├── entity/                         # 实体类
│   │   │   ├── User.java                  # 用户实体
│   │   │   ├── Post.java                  # 帖子实体
│   │   │   ├── Friend.java                # 好友关系实体
│   │   │   ├── Comment.java               # 评论实体
│   │   │   └── SystemNotice.java          # 系统通知实体
│   │   ├── mapper/                         # 数据访问层
│   │   │   ├── UserMapper.java            # 用户Mapper
│   │   │   ├── PostMapper.java            # 帖子Mapper
│   │   │   ├── FriendMapper.java          # 好友Mapper
│   │   │   ├── CommentMapper.java         # 评论Mapper
│   │   │   └── SystemNoticeMapper.java    # 系统通知Mapper
│   │   ├── service/                        # 服务层接口
│   │   │   ├── UserService.java           # 用户服务接口
│   │   │   ├── PostService.java           # 帖子服务接口
│   │   │   ├── FriendService.java         # 好友服务接口
│   │   │   ├── CommentService.java        # 评论服务接口
│   │   │   └── SystemNoticeService.java   # 系统通知服务接口
│   │   ├── service/impl/                   # 服务层实现
│   │   │   ├── UserServiceImpl.java       # 用户服务实现
│   │   │   ├── PostServiceImpl.java       # 帖子服务实现
│   │   │   ├── FriendServiceImpl.java     # 好友服务实现
│   │   │   ├── CommentServiceImpl.java    # 评论服务实现
│   │   │   └── SystemNoticeServiceImpl.java # 系统通知服务实现
│   │   └── vo/                            # 视图对象
│   │       ├── UserVO.java               # 用户VO
│   │       ├── PostVO.java               # 帖子VO
│   │       ├── FriendVO.java             # 好友VO
│   │       ├── CommentVO.java            # 评论VO
│   │       └── SystemNoticeVO.java       # 系统通知VO
│   └── resources/
│       ├── application.yml                 # 应用配置文件
│       └── mapper/                        # MyBatis映射文件
│           ├── UserMapper.xml             # 用户映射文件
│           ├── PostMapper.xml             # 帖子映射文件
│           ├── FriendMapper.xml           # 好友映射文件
│           ├── CommentMapper.xml          # 评论映射文件
│           └── SystemNoticeMapper.xml     # 系统通知映射文件
└── test/                                  # 测试代码
```

## 功能模块

### 1. 用户模块 (User)
- 用户注册、登录
- 用户信息管理
- 用户状态管理
- 用户列表查询

### 2. 帖子模块 (Post)
- 发布帖子（文字、图片、视频、链接）
- 帖子列表查询
- 帖子详情查看
- 帖子点赞、收藏、转发
- 帖子置顶、精华管理

### 3. 好友模块 (Friend)
- 好友申请、同意、拒绝
- 好友列表管理
- 好友备注设置
- 好友状态管理

### 4. 评论模块 (Comment)
- 发布评论
- 回复评论
- 评论点赞
- 评论管理

### 5. 系统模块 (System)
- 系统通知发布
- 通知推送管理
- 系统配置管理

## 快速开始

### 1. 环境要求
- JDK 8+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 2. 数据库配置
修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gongda_circle
    username: your_username
    password: your_password
```

### 3. Redis配置
修改 `application.yml` 中的Redis连接信息：
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password
```

### 4. 运行项目
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 5. 访问地址
- 应用地址: http://localhost:8080/api
- Druid监控: http://localhost:8080/api/druid

## API文档
项目启动后，可以通过以下方式查看API文档：
- 用户相关API: `/api/user/**`
- 帖子相关API: `/api/post/**`
- 好友相关API: `/api/friend/**`
- 评论相关API: `/api/comment/**`
- 系统相关API: `/api/system/**`

## 开发规范
1. 所有实体类继承 `BaseEntity`
2. 使用统一的响应格式 `Result<T>`
3. 使用 `@Validated` 进行参数校验
4. 使用 Lombok 简化代码
5. 遵循 RESTful API 设计规范

## 贡献指南
1. Fork 本仓库
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证
本项目采用 MIT 许可证