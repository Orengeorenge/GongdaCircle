# 工大圈项目部署文件

本目录包含用于将工大圈项目部署到宝塔面板的所有必要文件。

## 文件说明

1. `gongda_circle_deploy.sql` - 用于导入MySQL数据库的SQL文件
2. `application-prod.yml` - 生产环境配置文件
3. `deploy.sh` - 部署脚本
4. `DEPLOYMENT_GUIDE.md` - 详细的部署指南

## 快速部署步骤

1. 在宝塔面板中创建数据库并导入 `gongda_circle_deploy.sql`
2. 将 `application-prod.yml` 复制到项目的 `src/main/resources` 目录
3. 修改 `application-prod.yml` 中的数据库连接信息
4. 使用Maven打包项目：
   ```bash
   mvn clean package -Dmaven.test.skip=true -Pprod
   ```
5. 在前端目录中构建前端项目：
   ```bash
   cd frontend
   npm install
   npm run build
   ```
6. 使用部署脚本上传并部署：
   ```bash
   chmod +x deployment/deploy.sh
   ./deployment/deploy.sh ./target/gongda-circle-0.0.1-SNAPSHOT.jar ./frontend/dist
   ```

## 详细说明

请参考 `DEPLOYMENT_GUIDE.md` 获取完整的部署指南。 