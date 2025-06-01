# 好友功能API使用说明

## 好友关系接口

### 1. 申请添加好友
**接口地址：** `POST /api/friend/apply`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求体：**
```json
{
  "friendId": 2,
  "remark": "同班同学",
  "reason": "我们是同班同学，希望能加个好友"
}
```

**字段说明：**
- `friendId`: 要添加的好友用户ID（必填）
- `remark`: 备注信息（可选，最长200字符）
- `reason`: 申请理由（可选，最长500字符）

**响应示例：**
```json
{
  "code": 200,
  "message": "好友申请已发送",
  "data": true
}
```

### 2. 同意好友申请
**接口地址：** `POST /api/friend/{friendshipId}/approve`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `friendshipId`: 好友关系ID

**响应示例：**
```json
{
  "code": 200,
  "message": "已同意好友申请",
  "data": true
}
```

### 3. 拒绝好友申请
**接口地址：** `POST /api/friend/{friendshipId}/reject`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `friendshipId`: 好友关系ID

**响应示例：**
```json
{
  "code": 200,
  "message": "已拒绝好友申请",
  "data": true
}
```

### 4. 删除好友
**接口地址：** `DELETE /api/friend/{friendshipId}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `friendshipId`: 好友关系ID

**响应示例：**
```json
{
  "code": 200,
  "message": "已删除好友",
  "data": true
}
```

### 5. 获取好友列表
**接口地址：** `GET /api/friend/list`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求参数：**
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）
- `keyword`: 搜索关键词（可选，按用户名或昵称搜索）

**示例：** `GET /api/friend/list?page=1&size=10&keyword=张三`

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "friendId": 2,
        "friendInfo": {
          "id": 2,
          "username": "friend1",
          "nickname": "好友昵称",
          "avatar": "http://example.com/avatar.jpg",
          "school": "工业大学"
        },
        "status": 1,
        "statusDesc": "已通过",
        "remark": "同班同学",
        "reason": "我们是同班同学",
        "isInitiator": true,
        "createTime": "2024-01-01T12:00:00",
        "updateTime": "2024-01-01T12:30:00"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

### 6. 获取好友申请列表（收到的申请）
**接口地址：** `GET /api/friend/requests`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求参数：**
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [
      {
        "id": 5,
        "userId": 3,
        "friendId": 1,
        "friendInfo": {
          "id": 3,
          "username": "applicant",
          "nickname": "申请者",
          "avatar": "http://example.com/avatar.jpg"
        },
        "status": 0,
        "statusDesc": "待确认",
        "remark": "",
        "reason": "希望能加个好友",
        "isInitiator": false,
        "createTime": "2024-01-01T15:00:00"
      }
    ],
    "total": 3,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 7. 获取我发送的好友申请列表
**接口地址：** `GET /api/friend/sent`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求参数：**
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）

### 8. 检查是否为好友
**接口地址：** `GET /api/friend/check/{targetUserId}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `targetUserId`: 目标用户ID

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": true
}
```

### 9. 检查是否已申请好友
**接口地址：** `GET /api/friend/check-applied/{targetUserId}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `targetUserId`: 目标用户ID

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": false
}
```

### 10. 获取好友关系详情
**接口地址：** `GET /api/friend/{friendshipId}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**路径参数：**
- `friendshipId`: 好友关系ID

## 使用示例（curl）

### 申请添加好友
```bash
curl -X POST http://localhost:8080/api/friend/apply \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "friendId": 2,
    "remark": "同班同学",
    "reason": "我们是同班同学，希望能加个好友"
  }'
```

### 同意好友申请
```bash
curl -X POST http://localhost:8080/api/friend/1/approve \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 拒绝好友申请
```bash
curl -X POST http://localhost:8080/api/friend/1/reject \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 删除好友
```bash
curl -X DELETE http://localhost:8080/api/friend/1 \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 获取好友列表
```bash
curl -X GET "http://localhost:8080/api/friend/list?page=1&size=10" \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 获取好友申请列表
```bash
curl -X GET "http://localhost:8080/api/friend/requests?page=1&size=10" \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 检查是否为好友
```bash
curl -X GET http://localhost:8080/api/friend/check/2 \
  -H "Authorization: Bearer <your_jwt_token>"
```

## 好友状态说明

- **status字段：**
  - 0: 待确认
  - 1: 已通过
  - 2: 已拒绝
  - 3: 已删除

## 数据库表结构

```sql
CREATE TABLE friend (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '申请者用户ID',
  friend_id bigint(20) NOT NULL COMMENT '被申请者用户ID',
  status int(1) NOT NULL DEFAULT 0 COMMENT '状态 0-待确认 1-已通过 2-已拒绝 3-已删除',
  remark varchar(200) DEFAULT NULL COMMENT '备注信息',
  reason varchar(500) DEFAULT NULL COMMENT '申请理由',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  create_by bigint(20) DEFAULT NULL COMMENT '创建者',
  update_by bigint(20) DEFAULT NULL COMMENT '更新者',
  deleted tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_friend_id (friend_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友关系表';
```

## 业务规则

1. **申请限制：**
   - 不能添加自己为好友
   - 不能重复申请同一个用户
   - 目标用户必须存在且状态正常

2. **双向关系：**
   - 同意好友申请后，会自动创建双向好友关系
   - 删除好友时，会同时删除双向关系

3. **权限控制：**
   - 只有被申请者可以同意或拒绝好友申请
   - 只有好友关系的参与者可以删除好友关系

4. **状态流转：**
   - 待确认 → 已通过（同意申请）
   - 待确认 → 已拒绝（拒绝申请）
   - 已通过 → 已删除（删除好友）

## 注意事项

1. 所有接口都需要用户登录认证
2. 好友关系是双向的，同意申请后双方都会成为好友
3. 删除好友会同时删除双向关系
4. 可以通过搜索关键词查找好友（按用户名或昵称）
5. 支持分页查询，提高大量好友数据的加载性能
6. 申请理由和备注信息可以帮助用户更好地管理好友关系

## 错误代码说明

| 错误代码 | 说明 |
|---------|------|
| 200 | 操作成功 |
| 401 | 未登录或token无效 |
| 400 | 参数错误 |
| 500 | 服务器内部错误 |

常见错误信息：
- "不能添加自己为好友"
- "目标用户不存在"
- "你们已经是好友了"
- "好友申请已发送，请等待对方处理"
- "已经发送过好友申请，请勿重复申请"
- "无权限处理此好友申请"
- "好友申请状态无效" 