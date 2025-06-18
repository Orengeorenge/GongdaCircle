#!/bin/bash

# 工大圈项目部署脚本
# 使用方法: ./deploy.sh [jar文件路径] [前端dist目录路径]

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 检查参数
if [ $# -lt 2 ]; then
  echo -e "${RED}错误: 缺少必要参数${NC}"
  echo "使用方法: $0 [jar文件路径] [前端dist目录路径]"
  echo "示例: $0 ./target/gongda-circle-0.0.1-SNAPSHOT.jar ./frontend/dist"
  exit 1
fi

JAR_FILE=$1
DIST_DIR=$2
DEPLOY_DIR="/www/wwwroot/gongdacircle"

# 检查文件是否存在
if [ ! -f "$JAR_FILE" ]; then
  echo -e "${RED}错误: JAR文件 '$JAR_FILE' 不存在${NC}"
  exit 1
fi

if [ ! -d "$DIST_DIR" ]; then
  echo -e "${RED}错误: 前端目录 '$DIST_DIR' 不存在${NC}"
  exit 1
fi

# 创建部署目录
echo -e "${YELLOW}创建部署目录...${NC}"
mkdir -p $DEPLOY_DIR
mkdir -p $DEPLOY_DIR/frontend
mkdir -p $DEPLOY_DIR/uploads
mkdir -p $DEPLOY_DIR/logs

# 复制文件
echo -e "${YELLOW}复制后端JAR文件...${NC}"
cp $JAR_FILE $DEPLOY_DIR/gongda-circle.jar

echo -e "${YELLOW}复制前端文件...${NC}"
cp -r $DIST_DIR/* $DEPLOY_DIR/frontend/

# 设置权限
echo -e "${YELLOW}设置目录权限...${NC}"
chmod 755 -R $DEPLOY_DIR
chown -R www:www $DEPLOY_DIR

# 创建启动脚本
echo -e "${YELLOW}创建启动脚本...${NC}"
cat > $DEPLOY_DIR/start.sh << EOF
#!/bin/bash
cd $DEPLOY_DIR
nohup java -jar gongda-circle.jar --spring.profiles.active=prod > logs/startup.log 2>&1 &
echo \$! > app.pid
EOF

chmod +x $DEPLOY_DIR/start.sh

# 创建停止脚本
echo -e "${YELLOW}创建停止脚本...${NC}"
cat > $DEPLOY_DIR/stop.sh << EOF
#!/bin/bash
cd $DEPLOY_DIR
if [ -f app.pid ]; then
  PID=\$(cat app.pid)
  if ps -p \$PID > /dev/null; then
    echo "Stopping application (PID: \$PID)..."
    kill \$PID
    sleep 5
    if ps -p \$PID > /dev/null; then
      echo "Application still running, force killing..."
      kill -9 \$PID
    fi
  else
    echo "Application is not running (PID: \$PID)"
  fi
  rm app.pid
else
  echo "PID file not found"
fi
EOF

chmod +x $DEPLOY_DIR/stop.sh

# 创建重启脚本
echo -e "${YELLOW}创建重启脚本...${NC}"
cat > $DEPLOY_DIR/restart.sh << EOF
#!/bin/bash
cd $DEPLOY_DIR
./stop.sh
./start.sh
EOF

chmod +x $DEPLOY_DIR/restart.sh

echo -e "${GREEN}部署完成!${NC}"
echo -e "应用已部署到 ${YELLOW}$DEPLOY_DIR${NC}"
echo -e "请确保已在宝塔面板中配置Nginx和数据库"
echo -e "可以使用以下命令启动应用:"
echo -e "${YELLOW}cd $DEPLOY_DIR && ./start.sh${NC}"
echo -e "使用以下命令停止应用:"
echo -e "${YELLOW}cd $DEPLOY_DIR && ./stop.sh${NC}"
echo -e "使用以下命令重启应用:"
echo -e "${YELLOW}cd $DEPLOY_DIR && ./restart.sh${NC}"
echo -e "查看应用日志:"
echo -e "${YELLOW}tail -f $DEPLOY_DIR/logs/gongda-circle.log${NC}" 