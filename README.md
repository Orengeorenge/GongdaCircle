# 工大人社交圈 (GongdaCircle)

> 一个完整的大学生社交平台，包含Spring Boot后端服务和Vue3前端应用

## 🌟 项目简介

工大人社交圈是一个专为大学生打造的社交平台，提供帖子发布、好友互动、评论点赞等丰富功能。项目采用前后端分离架构，后端基于Spring Boot，前端使用Vue3 + Element Plus构建。

## 🚀 快速开始 (本地部署)

### 📋 环境要求
- **Java**: JDK 8+ 
- **Node.js**: 16.0+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+ (可选)
- **Maven**: 3.6+

### ⚡ 一键启动

#### 1. 克隆项目
```bash
git clone https://github.com/your-username/GongdaCircle.git
cd GongdaCircle
```

#### 2. 数据库配置
```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE gongda_circle CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库脚本
mysql -u root -p gongda_circle < sql/gongda_circle.sql
```

#### 3. 修改配置文件
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gongda_circle?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password
  redis:
    host: localhost
    port: 6379
    # password: your_redis_password  # 如果Redis设置了密码
```

#### 4. 启动后端服务
```bash
# 方式1: 使用Maven直接运行
mvn spring-boot:run

# 方式2: 打包后运行
mvn clean package -DskipTests
java -jar target/gongda-circle-1.0.0.jar
```
后端服务启动在：http://localhost:8080

#### 5. 启动前端服务
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```
前端应用启动在：http://localhost:3000

### 🎯 访问应用
- **前端界面**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **API文档**: http://localhost:8080/api/swagger-ui.html
- **数据库监控**: http://localhost:8080/api/druid

### 🔧 常见问题
- **端口冲突**: 修改 `application.yml` 中的 `server.port`
- **数据库连接失败**: 检查MySQL服务状态和密码配置
- **前端启动失败**: 删除 `node_modules` 重新安装依赖

## 🏗️ 项目架构

```
GongdaCircle/
├── src/                        # 后端 Spring Boot 代码
├── frontend/                   # 前端 Vue3 代码
├── sql/                        # 数据库脚本
├── docs/                       # 项目文档
└── README.md                   # 项目说明
```

## 🚀 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0 + MyBatis Plus 3.5.3.1
- **安全**: Spring Security + JWT
- **缓存**: Redis 6.0+
- **监控**: Druid + Swagger 3

### 前端技术栈
- **框架**: Vue 3.3+ + Vite 4.4+
- **UI组件**: Element Plus 2.4+
- **路由**: Vue Router 4 + Pinia 2.1+
- **HTTP**: Axios 1.5+

## ✨ 功能特性

### 🔐 用户系统
- 用户注册/登录、JWT Token 认证
- 个人资料管理、头像上传设置

### 📝 帖子功能
- 多类型帖子发布（文字、图片、视频、链接）
- 点赞、收藏、浏览量统计、话题标签系统

### 👥 好友系统
- 好友申请/同意/拒绝、好友列表管理
- 双向好友关系、推荐好友功能

### 💬 评论系统
- 一级评论和回复、评论点赞功能
- 评论管理和删除、防重复操作机制

### 🔔 系统通知
- 系统消息推送、通知状态管理

## 📚 API文档

- **[API使用说明](API_USAGE.md)** - 基础使用指南
- **[帖子API](POST_API.md)** - 帖子相关接口
- **[好友API](FRIEND_API.md)** - 好友系统接口
- **[评论API](COMMENT_API.md)** - 评论功能接口
- **[测试指南](TESTING_GUIDE.md)** - 接口测试说明

## 🛠️ 生产环境部署

### 📋 部署环境要求

#### 服务器配置建议
- **CPU**: 2核及以上
- **内存**: 4GB及以上
- **硬盘**: 50GB及以上可用空间

#### 软件环境要求
- **操作系统**: Linux (Ubuntu 20.04+ / CentOS 7+)
- **Java**: JDK 8+ (推荐 JDK 11)
- **Node.js**: 16.0+ (推荐 18.x LTS)
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nginx**: 1.18+

### 🔧 环境准备

#### 安装基础环境
```bash
# Ubuntu/Debian 一键安装
sudo apt update
sudo apt install openjdk-11-jdk mysql-server redis-server nginx maven

# 安装 Node.js (使用 NodeSource)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 💾 数据库配置

```bash
# 创建数据库和用户
mysql -u root -p
CREATE DATABASE gongda_circle CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'gongda_user'@'localhost' IDENTIFIED BY 'your_password_here';
GRANT ALL PRIVILEGES ON gongda_circle.* TO 'gongda_user'@'localhost';
FLUSH PRIVILEGES;

# 导入数据库脚本
mysql -u gongda_user -p gongda_circle < sql/gongda_circle.sql
```

### 🏠 后端部署

#### 配置文件 (application-prod.yml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gongda_circle?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: gongda_user
    password: your_password_here
  redis:
    host: localhost
    port: 6379
    password: your_redis_password

jwt:
  secret: your_jwt_secret_key_here_very_long_and_secure
  expiration: 86400

logging:
  file:
    name: /var/log/gongda-circle.log
```

#### 打包部署
```bash
# 打包项目
mvn clean package -DskipTests

# 创建部署目录
sudo mkdir -p /opt/gongda-circle
sudo cp target/gongda-circle-*.jar /opt/gongda-circle/

# 创建系统服务
sudo nano /etc/systemd/system/gongda-circle.service
```

#### 系统服务配置
```ini
[Unit]
Description=Gongda Circle Application
After=network.target mysql.service redis-server.service

[Service]
Type=simple
User=gongda
ExecStart=/usr/bin/java -jar /opt/gongda-circle/gongda-circle-1.0.0.jar --spring.profiles.active=prod
Restart=always

[Install]
WantedBy=multi-user.target
```

```bash
# 启动服务
sudo useradd -r gongda
sudo chown -R gongda:gongda /opt/gongda-circle
sudo systemctl daemon-reload
sudo systemctl enable gongda-circle
sudo systemctl start gongda-circle
```

### 🌐 前端部署

#### 构建前端
```bash
cd frontend
npm install
npm run build
```

#### Nginx 配置
```bash
sudo nano /etc/nginx/sites-available/gongda-circle
```

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /var/www/gongda-circle;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端 API 代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

```bash
# 部署前端文件
sudo mkdir -p /var/www/gongda-circle
sudo cp -r dist/* /var/www/gongda-circle/
sudo chown -R www-data:www-data /var/www/gongda-circle

# 启用站点
sudo ln -s /etc/nginx/sites-available/gongda-circle /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 🔐 SSL 证书 (可选)
```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取免费SSL证书
sudo certbot --nginx -d your-domain.com
```

### 🔍 部署验证
```bash
# 检查服务状态
sudo systemctl status gongda-circle
sudo systemctl status nginx
curl http://localhost:8080/api/health
```

## 🚨 故障排除

### 常见问题
1. **后端启动失败**: 检查端口占用 `sudo netstat -tlnp | grep 8080`
2. **数据库连接失败**: 检查MySQL服务 `sudo systemctl status mysql`
3. **前端访问异常**: 检查Nginx配置 `sudo nginx -t`

### 日志查看
```bash
# 后端日志
sudo journalctl -u gongda-circle -f

# Nginx日志
sudo tail -f /var/log/nginx/error.log
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
- ✅ **详细的部署指南**

## 📞 联系方式

- 项目名称：工大人社交圈 (GongdaCircle)
- 版本：v1.0.0

---

**Happy Coding! 🎉**