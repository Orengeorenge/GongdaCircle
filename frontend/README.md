# 工大人社交圈 - 前端项目

基于 Vue3 + Element Plus 构建的工大人社交圈前端应用。

## 技术栈

- **Vue 3** - 前端框架
- **Element Plus** - UI 组件库
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Vite** - 构建工具
- **Sass** - CSS 预处理器

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── components/         # 公共组件
│   │   └── Layout/         # 布局组件
│   │       └── AppHeader.vue
│   │   ├── views/              # 页面组件
│   │   │   ├── auth/           # 认证相关页面
│   │   │   │   ├── Login.vue   # 登录页面
│   │   │   │   └── Register.vue # 注册页面
│   │   │   ├── Home.vue        # 首页（帖子流）
│   │   │   ├── Publish.vue     # 发布帖子页面
│   │   │   ├── Friends.vue     # 好友管理页面
│   │   │   └── Profile.vue     # 用户中心页面
│   │   ├── router/             # 路由配置
│   │   │   └── index.js
│   │   ├── styles/             # 样式文件
│   │   │   └── global.scss     # 全局样式
│   │   ├── App.vue            # 根组件
│   │   └── main.js            # 入口文件
│   ├── index.html             # HTML 模板
│   ├── package.json           # 依赖配置
│   └── vite.config.js         # Vite 配置
└── README.md              # 项目说明
```

## 功能特性

### 🔐 用户认证
- 用户登录/注册
- JWT Token 认证
- 路由守卫保护

### 🏠 首页功能
- 帖子流展示
- 实时点赞/评论/收藏
- 无限滚动加载
- 热门话题推荐
- 推荐用户关注

### 📝 帖子发布
- 多种帖子类型（文字、图片、视频、链接）
- 话题标签管理
- 位置信息
- 实时预览
- 草稿保存

### 👥 好友管理
- 用户搜索
- 好友申请/同意/拒绝
- 好友列表管理
- 推荐好友

### 👤 用户中心
- 个人资料编辑
- 头像设置（上传/预设）
- 密码修改
- 登录日志查看
- 隐私设置

## 设计特色

### 🎨 视觉设计
- **深蓝 + 白色** 主色调
- 清晰简洁的界面设计
- 现代化的卡片式布局
- 优雅的交互动效

### 📱 响应式设计
- 移动端适配
- 平板端优化
- 桌面端完整体验

### 🔧 技术亮点
- Vue3 Composition API
- Element Plus 组件库
- CSS 变量主题系统
- 模块化 SCSS 样式
- 路由懒加载

## 安装运行

### 环境要求
- Node.js >= 16.0.0
- npm 或 yarn

### 安装依赖
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install
# 或使用 yarn
yarn install
```

### 开发运行
```bash
# 启动开发服务器
npm run dev
# 或使用 yarn
yarn dev
```

项目将在 http://localhost:3000 启动

### 构建部署
```bash
# 构建生产版本
npm run build
# 或使用 yarn
yarn build

# 预览构建结果
npm run preview
# 或使用 yarn
yarn preview
```

## 页面路由

| 路径 | 页面 | 描述 |
|------|------|------|
| `/login` | 登录页面 | 用户登录 |
| `/register` | 注册页面 | 用户注册 |
| `/home` | 首页 | 帖子流展示 |
| `/publish` | 发布页面 | 发布新帖子 |
| `/friends` | 好友页面 | 好友管理 |
| `/profile` | 用户中心 | 个人设置 |

## 模拟数据

项目目前使用模拟数据进行展示，包括：
- 用户信息
- 帖子列表
- 好友关系
- 登录日志
- 等等...

## 与后端集成

项目已配置代理，可以直接与后端 API 集成：

```javascript
// vite.config.js
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

只需要将模拟数据替换为真实的 API 调用即可。

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目名称：工大人社交圈
- 版本：1.0.0
- 作者：GongdaCircle Team 