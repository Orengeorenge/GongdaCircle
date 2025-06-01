# 评论、点赞、收藏功能 API 文档

## 概述

本文档描述了工大圈社交平台的评论、点赞、收藏功能相关的API接口。

## 基础信息

- **基础URL**: `http://localhost:8080/api`
- **认证方式**: JWT Token (在请求头中添加 `Authorization: Bearer <token>`)
- **数据格式**: JSON

## 评论功能 API

### 1. 发布评论

**接口地址**: `POST /comments/publish`

**请求参数**:
```json
{
    "postId": 1,
    "parentId": null,
    "replyUserId": null,
    "content": "这是一条评论内容",
    "images": "http://example.com/image1.jpg,http://example.com/image2.jpg"
}
```

**参数说明**:
- `postId`: 帖子ID（必填）
- `parentId`: 父评论ID（可选，用于回复评论）
- `replyUserId`: 回复用户ID（可选，用于@指定用户）
- `content`: 评论内容（必填，最大1000字符）
- `images`: 图片URL（可选，多张图片用逗号分隔，最大2000字符）

**响应示例**:
```json
{
    "code": 200,
    "message": "评论发布成功",
    "data": 123456789
}
```

### 2. 查询评论列表

**接口地址**: `GET /comments/list`

**请求参数**:
- `postId`: 帖子ID（必填）
- `page`: 页码（可选，默认1）
- `size`: 每页大小（可选，默认10）

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 123456789,
                "postId": 1,
                "userId": 2,
                "user": {
                    "id": 2,
                    "username": "student1",
                    "nickname": "学生一号",
                    "avatar": "http://example.com/avatar.jpg"
                },
                "parentId": null,
                "replyUserId": null,
                "replyUser": null,
                "content": "这是一条评论内容",
                "imageList": ["http://example.com/image1.jpg"],
                "likeCount": 5,
                "isLiked": false,
                "status": 1,
                "replies": [
                    {
                        "id": 123456790,
                        "content": "这是回复内容",
                        "user": {...},
                        "replyUser": {...}
                    }
                ],
                "createTime": "2024-01-01T12:00:00"
            }
        ],
        "total": 50,
        "size": 10,
        "current": 1,
        "pages": 5
    }
}
```

### 3. 删除评论

**接口地址**: `DELETE /comments/{commentId}`

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
    "code": 200,
    "message": "评论删除成功",
    "data": true
}
```

### 4. 点赞评论

**接口地址**: `POST /comments/{commentId}/like`

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
    "code": 200,
    "message": "点赞成功",
    "data": true
}
```

### 5. 取消点赞评论

**接口地址**: `DELETE /comments/{commentId}/like`

**路径参数**:
- `commentId`: 评论ID

**响应示例**:
```json
{
    "code": 200,
    "message": "取消点赞成功",
    "data": true
}
```

## 帖子点赞功能 API

### 1. 点赞帖子

**接口地址**: `POST /posts/{postId}/like`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "点赞成功",
    "data": true
}
```

### 2. 取消点赞帖子

**接口地址**: `DELETE /posts/{postId}/like`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "取消点赞成功",
    "data": true
}
```

### 3. 检查点赞状态

**接口地址**: `GET /posts/{postId}/like/status`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": true
}
```

### 4. 获取点赞数

**接口地址**: `GET /posts/{postId}/like/count`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": 25
}
```

## 帖子收藏功能 API

### 1. 收藏帖子

**接口地址**: `POST /posts/{postId}/collect`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "收藏成功",
    "data": true
}
```

### 2. 取消收藏帖子

**接口地址**: `DELETE /posts/{postId}/collect`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "取消收藏成功",
    "data": true
}
```

### 3. 检查收藏状态

**接口地址**: `GET /posts/{postId}/collect/status`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": false
}
```

### 4. 获取收藏数

**接口地址**: `GET /posts/{postId}/collect/count`

**路径参数**:
- `postId`: 帖子ID

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": 15
}
```

### 5. 获取我的收藏列表

**接口地址**: `GET /posts/collect/my`

**请求参数**:
- `page`: 页码（可选，默认1）
- `size`: 每页大小（可选，默认10）

**响应示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1,
                "title": "帖子标题",
                "content": "帖子内容",
                "user": {...},
                "likeCount": 10,
                "commentCount": 5,
                "collectCount": 3,
                "createTime": "2024-01-01T12:00:00"
            }
        ],
        "total": 20,
        "size": 10,
        "current": 1,
        "pages": 2
    }
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（需要登录） |
| 403 | 禁止访问（权限不足） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 使用示例

### 发布评论示例

```bash
curl -X POST "http://localhost:8080/api/comments/publish" \
  -H "Authorization: Bearer your_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "postId": 1,
    "content": "这是一条测试评论"
  }'
```

### 点赞帖子示例

```bash
curl -X POST "http://localhost:8080/api/posts/1/like" \
  -H "Authorization: Bearer your_jwt_token"
```

### 收藏帖子示例

```bash
curl -X POST "http://localhost:8080/api/posts/1/collect" \
  -H "Authorization: Bearer your_jwt_token"
```

## 注意事项

1. 所有接口都需要用户登录认证
2. 评论内容不能为空，最大长度1000字符
3. 重复点赞/收藏会返回相应的提示信息
4. 只有评论作者可以删除自己的评论
5. 评论支持嵌套回复，最多支持二级回复
6. 图片URL需要是有效的网络地址 