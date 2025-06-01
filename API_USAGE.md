# 工大圈API使用说明

## 认证机制
本系统使用JWT (JSON Web Token) 进行用户认证，所有需要认证的接口都需要在请求头中携带Token。

### Token格式
```
Authorization: Bearer <your_jwt_token>
```

## 用户相关API

### 1. 用户注册
**接口地址：** `POST /api/user/register`

**请求体：**
```json
{
  "username": "testuser",
  "password": "TestPass123",
  "nickname": "测试用户",
  "email": "test@gongda.edu.cn",
  "phone": "13800138000",
  "gender": 1,
  "school": "工业大学",
  "major": "计算机科学与技术",
  "grade": "2024"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "注册成功",
  "data": true
}
```

### 2. 用户登录
**接口地址：** `POST /api/user/login`

**请求体：**
```json
{
  "username": "testuser",
  "password": "TestPass123"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "userInfo": {
      "id": 1,
      "username": "testuser",
      "nickname": "测试用户",
      "email": "test@gongda.edu.cn",
      "phone": "13800138000",
      "avatar": null,
      "gender": 1,
      "birthday": null,
      "biography": null,
      "school": "工业大学",
      "major": "计算机科学与技术",
      "grade": "2024",
      "status": 1,
      "createTime": "2024-01-01T12:00:00"
    }
  }
}
```

### 3. 获取当前用户信息
**接口地址：** `GET /api/auth/me`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "email": "test@gongda.edu.cn",
    "phone": "13800138000",
    "avatar": null,
    "gender": 1,
    "birthday": null,
    "biography": null,
    "school": "工业大学",
    "major": "计算机科学与技术",
    "grade": "2024",
    "status": 1,
    "createTime": "2024-01-01T12:00:00"
  }
}
```

### 4. 检查用户名是否存在
**接口地址：** `GET /api/user/check/username?username=testuser`

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

### 5. 检查邮箱是否存在
**接口地址：** `GET /api/user/check/email?email=test@gongda.edu.cn`

### 6. 检查手机号是否存在
**接口地址：** `GET /api/user/check/phone?phone=13800138000`

### 7. 获取用户信息（需认证）
**接口地址：** `GET /api/user/{id}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

### 8. 更新用户信息（需认证）
**接口地址：** `PUT /api/user/{id}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求体：**
```json
{
  "username": "testuser",
  "nickname": "测试用户更新",
  "email": "newemail@gongda.edu.cn",
  "phone": "13900139000",
  "gender": 1,
  "biography": "这是我的个人简介",
  "school": "工业大学",
  "major": "软件工程",
  "grade": "2024"
}
```

### 9. 分页查询用户列表（需认证）
**接口地址：** `GET /api/user/list?page=1&size=10&keyword=test`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

### 10. 测试认证
**接口地址：** `GET /api/auth/test`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

## 错误代码说明

| 错误代码 | 说明 |
|---------|------|
| 200 | 操作成功 |
| 401 | 未授权访问，需要登录 |
| 500 | 服务器内部错误 |

## 使用示例（curl）

### 注册用户
```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123",
    "nickname": "测试用户",
    "email": "test@gongda.edu.cn",
    "phone": "13800138000",
    "gender": 1,
    "school": "工业大学",
    "major": "计算机科学与技术",
    "grade": "2024"
  }'
```

### 用户登录
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123"
  }'
```

### 获取当前用户信息
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 测试认证
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer <your_jwt_token>"
```

## 注意事项

1. **密码要求：** 密码必须包含大小写字母和数字，长度8-20位
2. **用户名要求：** 只能包含字母、数字、下划线，长度3-20位
3. **Token过期时间：** 24小时（86400000毫秒）
4. **数据库：** 使用前请先执行 `sql/init.sql` 初始化数据库
5. **测试账号：** 
   - 管理员：admin / 123456
   - 学生账号：student1 / 123456
   - 学生账号：student2 / 123456

## 前端集成建议

1. 登录成功后，将token保存到localStorage或sessionStorage
2. 每次请求时在请求头中添加Authorization字段
3. 监听401错误，自动跳转到登录页面
4. Token过期前可以实现自动刷新机制 