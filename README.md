# 工大人社交圈 (GongdaCircle)

> 一个完整的大学生社交平台，包含Spring Boot后端服务和Vue3前端应用

## 🌟 项目简介

工大人社交圈是一个专为大学生打造的社交平台，提供帖子发布、好友互动、评论点赞等丰富功能。项目采用前后端分离架构，后端基于Spring Boot，前端使用Vue3 + Element Plus构建。

## 🏗️ 项目架构

```
GongdaCircle/
├── src/                        # 后端 Spring Boot 代码
├── frontend/                   # 前端 Vue3 代码
├── sql/                        # 数据库脚本
├── API_USAGE.md               # API 使用说明
├── POST_API.md                # 帖子 API 文档
├── FRIEND_API.md              # 好友 API 文档
├── COMMENT_API.md             # 评论 API 文档
├── TESTING_GUIDE.md           # 测试指南
└── README.md                  # 项目说明
```

## 🚀 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.3.1
- **连接池**: Druid 1.2.16
- **安全**: Spring Security + JWT
- **缓存**: Redis
- **工具库**: Hutool 5.8.16
- **JSON**: FastJSON 2.0.25

### 前端技术栈
- **框架**: Vue 3
- **UI组件**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **构建工具**: Vite
- **样式**: Sass/SCSS

## ✨ 功能特性

### 🔐 用户系统
- 用户注册/登录
- JWT Token 认证
- 个人资料管理
- 头像上传/设置
- 隐私设置

### 📝 帖子功能
- 多类型帖子发布（文字、图片、视频、链接）
- 帖子列表查询和分页
- 点赞、收藏、浏览量统计
- 话题标签系统
- 置顶和精华帖管理

### 👥 好友系统
- 好友申请/同意/拒绝
- 好友列表管理
- 双向好友关系
- 好友状态展示
- 推荐好友功能

### 💬 评论系统
- 一级评论和回复
- 评论点赞功能
- 评论管理和删除
- 防重复操作机制

### 🔔 系统通知
- 系统消息推送
- 通知状态管理
- 通知分类展示

## 🎨 前端界面

### 页面展示
- **登录/注册页** - 优雅的认证界面
- **首页** - 帖子流展示，热门话题推荐
- **发布页** - 多媒体内容发布，实时预览
- **好友页** - 用户搜索，好友管理
- **用户中心** - 个人设置，隐私配置

### 设计特色
- 深蓝 + 白色主色调
- 现代化卡片式布局
- 完整的响应式设计
- 优雅的交互动效

## 🛠️ 快速开始

### 环境要求
- **后端**: JDK 8+, MySQL 8.0+, Redis 6.0+, Maven 3.6+
- **前端**: Node.js 16+, npm/yarn

### 1. 数据库配置
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE gongda_circle CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库脚本
mysql -u root -p gongda_circle < sql/gongda_circle.sql
```

### 2. 后端启动
```bash
# 修改 application.yml 中的数据库和Redis配置
# 然后启动后端服务
mvn clean compile
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 3. 前端启动
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端应用将在 http://localhost:3000 启动

### 4. 访问应用
- **前端界面**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **Druid监控**: http://localhost:8080/api/druid

## 📚 API文档

项目提供完整的API文档：

- **[API使用说明](API_USAGE.md)** - 基础使用指南
- **[帖子API](POST_API.md)** - 帖子相关接口
- **[好友API](FRIEND_API.md)** - 好友系统接口
- **[评论API](COMMENT_API.md)** - 评论功能接口
- **[测试指南](TESTING_GUIDE.md)** - 接口测试说明

## 🔧 开发规范

### 后端规范
- 所有实体类继承 `BaseEntity`
- 使用统一响应格式 `Result<T>`
- 遵循 RESTful API 设计
- 使用 `@Validated` 进行参数校验
- 统一异常处理机制

### 前端规范
- Vue3 Composition API
- TypeScript 类型定义
- 组件化开发
- 统一的样式规范
- 响应式设计原则

## 📦 部署说明

### 后端部署
```bash
# 打包项目
mvn clean package -DskipTests

# 运行jar包
java -jar target/gongda-circle-1.0.0.jar
```

### 前端部署
```bash
# 构建生产版本
cd frontend
npm run build

# 部署dist目录到Web服务器
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)

## 🎯 项目特点

- ✅ **完整的前后端分离架构**
- ✅ **现代化的技术栈**
- ✅ **丰富的功能模块**
- ✅ **优雅的界面设计**
- ✅ **完善的API文档**
- ✅ **规范的代码结构**
- ✅ **详细的开发指南**

## 📞 联系方式

- 项目名称：工大人社交圈 (GongdaCircle)
- 版本：v1.0.0
- 维护团队：GongdaCircle Team

---

**Happy Coding! 🎉**