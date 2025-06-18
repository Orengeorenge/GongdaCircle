# 工大圈项目宝塔面板部署指南

本指南将帮助你使用宝塔面板将工大圈项目部署到云服务器上。

## 一、准备工作

### 1. 服务器要求
- 操作系统：CentOS 7+ 或 Ubuntu 18.04+
- 内存：至少2GB RAM
- CPU：至少1核
- 硬盘：至少20GB可用空间
- 带宽：建议至少1Mbps

### 2. 安装宝塔面板
如果你还没有安装宝塔面板，请参考[宝塔官方安装教程](https://www.bt.cn/bbs/thread-19376-1-1.html)。

## 二、环境配置

### 1. 安装必要的软件
在宝塔面板中安装以下软件：
- Nginx（推荐1.18+）
- MySQL（推荐5.7或8.0）
- JDK（推荐1.8或11）
- Redis（可选，推荐6.0+）

### 2. 创建数据库
1. 在宝塔面板中，进入"数据库"页面
2. 点击"添加数据库"
3. 填写以下信息：
   - 数据库名：gongda_circle
   - 用户名：gongdacircle（或其他你喜欢的用户名）
   - 密码：设置一个强密码
   - 访问权限：本地或所有人（根据需要选择）
4. 点击"提交"创建数据库

### 3. 导入数据库
1. 在宝塔面板中，进入"数据库"页面
2. 找到刚创建的数据库，点击"导入"
3. 上传本项目中的 `sql/gongda_circle_deploy.sql` 文件
4. 点击"导入"按钮开始导入

## 三、后端部署

### 1. 打包Spring Boot项目
在你的开发环境中：
1. 将 `deployment/application-prod.yml` 文件复制到 `src/main/resources/` 目录下
2. 修改 `application-prod.yml` 中的数据库连接信息，确保用户名和密码与你在宝塔面板创建的一致
3. 使用Maven打包项目：
   ```bash
   mvn clean package -Dmaven.test.skip=true -Pprod
   ```
4. 打包完成后，在 `target` 目录下会生成 `gongda-circle-0.0.1-SNAPSHOT.jar` 文件

### 2. 上传并部署后端
1. 在宝塔面板中，创建目录 `/www/wwwroot/gongdacircle`
2. 上传打包好的jar文件到该目录
3. 在宝塔面板中，进入"软件商店" -> "PM2管理器" -> "安装"
4. 安装完成后，进入PM2管理器，点击"添加项目"
5. 填写以下信息：
   - 项目名称：gongdacircle
   - 启动文件：/www/wwwroot/gongdacircle/gongda-circle-0.0.1-SNAPSHOT.jar
   - 运行目录：/www/wwwroot/gongdacircle
   - 项目参数：--spring.profiles.active=prod
   - 运行用户：www
   - 开机自启：是
6. 点击"提交"启动项目

### 3. 创建必要的目录
在服务器上创建以下目录：
```bash
mkdir -p /www/wwwroot/gongdacircle/uploads
mkdir -p /www/wwwroot/gongdacircle/logs
chmod 755 -R /www/wwwroot/gongdacircle
chown -R www:www /www/wwwroot/gongdacircle
```

## 四、前端部署

### 1. 打包前端项目
在你的开发环境中：
1. 进入前端项目目录：
   ```bash
   cd frontend
   ```
2. 安装依赖：
   ```bash
   npm install
   ```
3. 修改 `.env.production` 文件，设置正确的API地址：
   ```
   VITE_API_BASE_URL=http://你的域名/api
   ```
4. 构建生产版本：
   ```bash
   npm run build
   ```
5. 构建完成后，在 `dist` 目录下会生成前端静态文件

### 2. 上传并部署前端
1. 在宝塔面板中，创建网站，域名填写你的域名
2. 网站目录设置为 `/www/wwwroot/gongdacircle/frontend`
3. 上传 `dist` 目录下的所有文件到 `/www/wwwroot/gongdacircle/frontend` 目录

### 3. 配置Nginx
1. 在宝塔面板中，进入"网站" -> 你的网站 -> "设置" -> "配置文件"
2. 修改配置文件如下：

```nginx
server {
    listen 80;
    server_name 你的域名;
    root /www/wwwroot/gongdacircle/frontend;
    index index.html;

    # 前端静态文件
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后端API
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 上传文件目录
    location /uploads/ {
        alias /www/wwwroot/gongdacircle/uploads/;
    }

    # 禁止访问敏感文件
    location ~ ^/(\.user.ini|\.htaccess|\.git|\.svn|\.project|LICENSE|README.md) {
        return 404;
    }
}
```

3. 保存配置并重启Nginx

## 五、配置HTTPS（可选但推荐）

1. 在宝塔面板中，进入"网站" -> 你的网站 -> "设置" -> "SSL"
2. 选择"Let's Encrypt"，申请免费的SSL证书
3. 勾选"强制HTTPS"
4. 点击"保存"

## 六、测试部署

1. 在浏览器中访问你的域名，应该能看到工大圈的前端页面
2. 尝试注册和登录，测试功能是否正常
3. 检查后端日志：
   ```bash
   tail -f /www/wwwroot/gongdacircle/logs/gongda-circle.log
   ```

## 七、常见问题排查

### 1. 后端无法启动
- 检查数据库连接信息是否正确
- 检查JDK版本是否兼容
- 查看日志文件中的错误信息

### 2. 前端无法访问后端API
- 检查Nginx配置是否正确
- 确认后端服务是否正常运行
- 检查防火墙是否开放了8080端口

### 3. 文件上传失败
- 检查上传目录权限是否正确
- 确认application-prod.yml中的上传路径配置是否正确

### 4. 数据库连接问题
- 确认MySQL服务是否正常运行
- 检查数据库用户权限是否正确
- 尝试使用命令行连接数据库测试

## 八、维护建议

1. 定期备份数据库
2. 监控服务器资源使用情况
3. 定期检查日志文件，及时发现并解决问题
4. 设置日志轮转，避免日志文件过大
5. 配置防火墙，只开放必要的端口

## 九、安全建议

1. 修改默认的数据库密码和管理员密码
2. 禁用不必要的服务和端口
3. 配置SSL证书，使用HTTPS
4. 定期更新系统和软件包
5. 设置适当的文件权限 