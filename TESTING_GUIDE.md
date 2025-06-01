# 工大圈项目测试指南

## 环境准备

### 1. 数据库准备
确保MySQL服务已启动，执行以下命令创建数据库：

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source sql/init.sql
```

### 2. 启动项目
```bash
# 在项目根目录执行
mvn spring-boot:run

# 或者使用IDE直接运行 GongdaCircleApplication.java
```

项目启动后，访问地址：http://localhost:8080

## 测试工具

### 推荐工具：
1. **Postman** - 最常用的API测试工具
2. **Apifox** - 国产API测试工具，界面友好
3. **curl** - 命令行工具
4. **VS Code REST Client** - VS Code插件

## 完整测试流程

### 第一步：用户注册和登录

#### 1.1 注册用户
```bash
# 注册用户A
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "password": "TestPass123",
    "nickname": "测试用户1",
    "email": "testuser1@gongda.edu.cn",
    "phone": "13900000001",
    "gender": 1,
    "school": "工业大学",
    "major": "计算机科学与技术",
    "grade": "2024"
  }'

# 注册用户B
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser2",
    "password": "TestPass123",
    "nickname": "测试用户2",
    "email": "testuser2@gongda.edu.cn",
    "phone": "13900000002",
    "gender": 0,
    "school": "工业大学",
    "major": "软件工程",
    "grade": "2023"
  }'
```

#### 1.2 用户登录获取Token
```bash
# 用户A登录
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser1",
    "password": "TestPass123"
  }'

# 返回结果会包含token，复制保存备用
# {
#   "code": 200,
#   "message": "登录成功",
#   "data": {
#     "token": "eyJhbGciOiJIUzUxMiJ9...",
#     ...
#   }
# }

# 用户B登录
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser2",
    "password": "TestPass123"
  }'
```

### 第二步：测试好友功能

#### 2.1 用户A申请添加用户B为好友
```bash
# 替换 <userA_token> 为用户A的实际token
# 替换 <userB_id> 为用户B的实际ID（从登录响应中获取）
curl -X POST http://localhost:8080/api/friend/apply \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <userA_token>" \
  -d '{
    "friendId": <userB_id>,
    "remark": "同班同学",
    "reason": "我们是同班同学，希望能成为好友一起学习"
  }'
```

#### 2.2 用户B查看收到的好友申请
```bash
# 替换 <userB_token> 为用户B的实际token
curl -X GET "http://localhost:8080/api/friend/requests?page=1&size=10" \
  -H "Authorization: Bearer <userB_token>"
```

#### 2.3 用户B同意好友申请
```bash
# 替换 <userB_token> 为用户B的实际token
# 替换 <friendship_id> 为好友关系ID（从申请列表中获取）
curl -X POST http://localhost:8080/api/friend/<friendship_id>/approve \
  -H "Authorization: Bearer <userB_token>"
```

#### 2.4 查看好友列表
```bash
# 用户A查看好友列表
curl -X GET "http://localhost:8080/api/friend/list?page=1&size=10" \
  -H "Authorization: Bearer <userA_token>"

# 用户B查看好友列表
curl -X GET "http://localhost:8080/api/friend/list?page=1&size=10" \
  -H "Authorization: Bearer <userB_token>"
```

#### 2.5 检查好友关系
```bash
# 用户A检查是否与用户B为好友
curl -X GET http://localhost:8080/api/friend/check/<userB_id> \
  -H "Authorization: Bearer <userA_token>"
```

### 第三步：测试帖子功能

#### 3.1 发布帖子
```bash
# 用户A发布文字帖子
curl -X POST http://localhost:8080/api/post/publish \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <userA_token>" \
  -d '{
    "title": "今天学习Spring Boot的心得",
    "content": "今天学习了Spring Boot的自动配置原理，收获很大！特别是关于好友功能的实现，学到了很多东西。",
    "type": 1,
    "tags": "学习,Spring Boot,编程,好友功能",
    "location": "工业大学图书馆"
  }'
```

#### 3.2 查看帖子列表
```bash
# 查看所有帖子
curl -X GET "http://localhost:8080/api/post/list?page=1&size=10"

# 查看我的帖子
curl -X GET "http://localhost:8080/api/post/my?page=1&size=10" \
  -H "Authorization: Bearer <userA_token>"
```

#### 3.3 点赞和收藏帖子
```bash
# 用户B点赞用户A的帖子
# 替换 <post_id> 为实际帖子ID
curl -X POST http://localhost:8080/api/post/<post_id>/like \
  -H "Authorization: Bearer <userB_token>"

# 用户B收藏用户A的帖子
curl -X POST http://localhost:8080/api/post/<post_id>/collect \
  -H "Authorization: Bearer <userB_token>"
```

## 测试场景

### 场景1：正常好友申请流程
1. 用户A申请添加用户B为好友
2. 用户B收到申请通知
3. 用户B同意申请
4. 双方成为好友
5. 可以在好友列表中看到对方

### 场景2：拒绝好友申请
1. 用户A申请添加用户B为好友
2. 用户B拒绝申请
3. 用户A在发送的申请列表中看到被拒绝状态

### 场景3：删除好友关系
1. 已成为好友的用户A和用户B
2. 用户A删除好友关系
3. 双方都不再显示为好友

### 场景4：重复申请防护
1. 用户A申请添加用户B为好友
2. 用户A再次申请添加用户B
3. 系统提示已经发送过申请

### 场景5：异常情况测试
1. 申请添加自己为好友（应该失败）
2. 申请添加不存在的用户（应该失败）
3. 非被申请者处理好友申请（应该失败）

## 使用Postman测试

### 1. 创建Collection
在Postman中创建一个新的Collection，命名为"工大圈API测试"

### 2. 设置环境变量
创建环境变量：
- `base_url`: http://localhost:8080/api
- `userA_token`: 用户A的JWT token
- `userB_token`: 用户B的JWT token
- `userA_id`: 用户A的ID
- `userB_id`: 用户B的ID

### 3. 导入预设请求
可以创建以下预设请求：

#### 用户注册
- Method: POST
- URL: `{{base_url}}/user/register`
- Body: JSON格式的用户注册信息

#### 用户登录
- Method: POST
- URL: `{{base_url}}/user/login`
- Body: JSON格式的登录信息
- Tests: 自动提取token到环境变量

#### 申请好友
- Method: POST
- URL: `{{base_url}}/friend/apply`
- Headers: `Authorization: Bearer {{userA_token}}`
- Body: JSON格式的好友申请信息

#### 处理好友申请
- Method: POST
- URL: `{{base_url}}/friend/{{friendship_id}}/approve`
- Headers: `Authorization: Bearer {{userB_token}}`

## 预期测试结果

### 成功场景预期结果：
1. 用户注册成功，返回200状态码
2. 用户登录成功，返回JWT token
3. 好友申请发送成功
4. 好友申请处理成功
5. 好友列表正确显示好友信息
6. 帖子发布和交互功能正常

### 失败场景预期结果：
1. 重复申请返回错误提示
2. 权限验证失败返回401
3. 参数验证失败返回具体错误信息

## 常见问题排查

### 1. 数据库连接问题
- 检查MySQL服务是否启动
- 检查application.yml中的数据库配置
- 确认数据库和表已正确创建

### 2. Token相关问题
- 检查JWT配置是否正确
- 确认Token未过期
- 验证请求头格式：`Authorization: Bearer <token>`

### 3. 接口404问题
- 检查Spring Boot是否正常启动
- 确认接口路径是否正确
- 检查Controller是否被Spring扫描到

### 4. 权限问题
- 确认SecurityConfig中的接口权限配置
- 检查JWT过滤器是否正常工作
- 验证用户身份认证状态

## 自动化测试脚本

可以创建简单的测试脚本来自动化测试流程：

```bash
#!/bin/bash
# 自动化测试脚本示例

# 1. 注册用户
echo "正在注册测试用户..."
# 注册命令...

# 2. 登录获取token
echo "正在登录获取token..."
# 登录命令...

# 3. 测试好友功能
echo "正在测试好友功能..."
# 好友申请命令...
# 处理申请命令...

# 4. 验证结果
echo "正在验证测试结果..."
# 验证命令...

echo "测试完成！"
```

这样您就可以完整地测试整个好友功能了！ 