# 帖子相关API使用说明

## 帖子接口

### 1. 发布帖子
**接口地址：** `POST /api/post/publish`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求体：**
```json
{
  "title": "这是帖子标题",
  "content": "这是帖子内容，可以很长很长...",
  "type": 1,
  "tags": "学习,编程,Spring Boot",
  "images": "http://example.com/image1.jpg,http://example.com/image2.jpg",
  "location": "工业大学"
}
```

**字段说明：**
- `title`: 帖子标题（可选，最长200字符）
- `content`: 帖子内容（必填，最长5000字符）
- `type`: 帖子类型（必填）
  - 1: 文字帖子
  - 2: 图片帖子
  - 3: 视频帖子
  - 4: 链接帖子
- `tags`: 话题标签（可选，多个标签用逗号分隔）
- `images`: 图片URL（可选，多张图片用逗号分隔）
- `video`: 视频URL（可选，type=3时使用）
- `linkUrl`: 链接URL（可选，type=4时使用）
- `linkTitle`: 链接标题（可选，type=4时使用）
- `linkDescription`: 链接描述（可选，type=4时使用）
- `location`: 位置信息（可选）

**响应示例：**
```json
{
  "code": 200,
  "message": "发布成功",
  "data": true
}
```

### 2. 获取帖子详情
**接口地址：** `GET /api/post/{id}`

**响应示例：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 1,
    "userInfo": {
      "id": 1,
      "username": "testuser",
      "nickname": "测试用户",
      "avatar": "http://example.com/avatar.jpg"
    },
    "title": "这是帖子标题",
    "content": "这是帖子内容",
    "type": 1,
    "tags": "学习,编程,Spring Boot",
    "images": "http://example.com/image1.jpg,http://example.com/image2.jpg",
    "location": "工业大学",
    "isTop": 0,
    "isEssence": 0,
    "status": 1,
    "likeCount": 10,
    "commentCount": 5,
    "collectCount": 3,
    "shareCount": 2,
    "viewCount": 100,
    "createTime": "2024-01-01T12:00:00",
    "updateTime": "2024-01-01T12:00:00"
  }
}
```

### 3. 分页查询帖子列表
**接口地址：** `GET /api/post/list`

**请求参数：**
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）
- `keyword`: 搜索关键词（可选）
- `userId`: 用户ID（可选，查询指定用户的帖子）
- `type`: 帖子类型（可选）

**示例：** `GET /api/post/list?page=1&size=10&keyword=学习&type=1`

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
        "userInfo": {
          "id": 1,
          "username": "testuser",
          "nickname": "测试用户"
        },
        "title": "这是帖子标题",
        "content": "这是帖子内容",
        "type": 1,
        "tags": "学习,编程",
        "likeCount": 10,
        "commentCount": 5,
        "viewCount": 100,
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

### 4. 获取我的帖子列表
**接口地址：** `GET /api/post/my`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求参数：**
- `page`: 页码（默认1）
- `size`: 每页数量（默认10）
- `keyword`: 搜索关键词（可选）
- `type`: 帖子类型（可选）

### 5. 更新帖子
**接口地址：** `PUT /api/post/{id}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**请求体：**
```json
{
  "title": "更新后的标题",
  "content": "更新后的内容",
  "type": 1,
  "tags": "更新,编程",
  "location": "工业大学"
}
```

**注意：** 只有帖子作者才能更新自己的帖子。

### 6. 删除帖子
**接口地址：** `DELETE /api/post/{id}`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**注意：** 只有帖子作者才能删除自己的帖子。删除为逻辑删除。

### 7. 点赞帖子
**接口地址：** `POST /api/post/{id}/like`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**响应示例：**
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": true
}
```

### 8. 收藏帖子
**接口地址：** `POST /api/post/{id}/collect`

**请求头：**
```
Authorization: Bearer <your_jwt_token>
```

**响应示例：**
```json
{
  "code": 200,
  "message": "收藏成功",
  "data": true
}
```

## 使用示例（curl）

### 发布文字帖子
```bash
curl -X POST http://localhost:8080/api/post/publish \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "title": "今天学习Spring Boot的心得",
    "content": "今天学习了Spring Boot的自动配置原理，收获很大！",
    "type": 1,
    "tags": "学习,Spring Boot,编程",
    "location": "工业大学图书馆"
  }'
```

### 发布图片帖子
```bash
curl -X POST http://localhost:8080/api/post/publish \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "title": "校园美景",
    "content": "今天天气很好，拍了一些校园的美景",
    "type": 2,
    "tags": "校园,美景,摄影",
    "images": "http://example.com/image1.jpg,http://example.com/image2.jpg",
    "location": "工业大学"
  }'
```

### 获取帖子列表
```bash
curl -X GET "http://localhost:8080/api/post/list?page=1&size=10&keyword=学习"
```

### 获取我的帖子
```bash
curl -X GET "http://localhost:8080/api/post/my?page=1&size=10" \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 点赞帖子
```bash
curl -X POST http://localhost:8080/api/post/1/like \
  -H "Authorization: Bearer <your_jwt_token>"
```

### 收藏帖子
```bash
curl -X POST http://localhost:8080/api/post/1/collect \
  -H "Authorization: Bearer <your_jwt_token>"
```

## 帖子状态说明

- **status字段：**
  - 0: 草稿
  - 1: 已发布
  - 2: 已删除
  - 3: 已封禁

- **type字段：**
  - 1: 文字帖子
  - 2: 图片帖子  
  - 3: 视频帖子
  - 4: 链接帖子

- **isTop字段：**
  - 0: 非置顶
  - 1: 置顶

- **isEssence字段：**
  - 0: 非精华
  - 1: 精华

## 业务规则

1. **发布权限：** 用户必须登录且状态正常才能发布帖子
2. **修改权限：** 只有帖子作者才能修改和删除自己的帖子
3. **内容限制：** 帖子内容不能超过5000字符，标题不能超过200字符
4. **自动计数：** 浏览量会在用户查看帖子详情时自动增加
5. **排序规则：** 帖子列表按置顶优先，然后按创建时间倒序排列

## 注意事项

1. 所有需要身份验证的接口都需要在请求头中携带JWT Token
2. 帖子的图片、视频等媒体文件需要先上传到文件服务器，然后将URL传给接口
3. 标签建议使用中文逗号分隔，便于后续搜索和分类
4. 删除是逻辑删除，数据仍保留在数据库中
5. 点赞和收藏功能目前是简单实现，实际项目中需要防止重复操作 