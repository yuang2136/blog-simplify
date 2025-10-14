# 博客简化系统 - API 接口文档

## 项目概述

**项目名称**: blog-simplify  
**技术栈**: Spring Boot 3.3.3 + MyBatis Plus + MySQL + JWT + Redis  
**Java版本**: 17  
**描述**: 基于Spring Boot的简化博客系统，支持用户注册登录和博客文章管理

## 认证说明

### JWT Token 认证
- 除了用户注册和登录接口外，所有其他接口都需要在请求头中携带有效的JWT Token
- Token格式: `Authorization: Bearer <token>`
- Token失效时间: 根据JwtUtil配置（通常为24小时）

### 受保护的路径
- `/blog/**` - 所有博客相关接口都需要认证
- 例外路径: `/user/login`, `/user/register` - 无需认证

## 基础信息

### 服务器配置
- **开发环境**: http://localhost:8080
- **Content-Type**: application/json
- **字符编码**: UTF-8

### 通用响应格式
```json
{
  "success": true/false,
  "message": "操作结果描述",
  "data": "具体数据"
}
```

## 用户管理接口

### 1. 用户注册

**接口地址**: `POST /user/register`  
**功能描述**: 用户注册新账户  
**权限要求**: 无需认证

#### 请求参数
```json
{
  "username": "用户名",
  "password": "密码"
}
```

#### 响应示例
```json
"注册成功"
```
或
```json
"注册失败"
```

#### 状态码
- `200`: 请求成功
- `400`: 请求参数错误
- `500`: 服务器内部错误

---

### 2. 用户登录

**接口地址**: `POST /user/login`  
**功能描述**: 用户登录获取JWT Token  
**权限要求**: 无需认证

#### 请求参数
```json
{
  "username": "用户名",
  "password": "密码"
}
```

#### 响应示例
成功时返回JWT Token:
```json
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

失败时返回错误信息:
```json
"用户名或密码错误"
```

#### 状态码
- `200`: 请求成功
- `401`: 用户名或密码错误
- `400`: 请求参数错误

---

## 博客管理接口

> ⚠️ **注意**: 以下所有接口都需要在请求头中携带有效的JWT Token

### 1. 获取博客列表

**接口地址**: `GET /blog/list`  
**功能描述**: 获取所有博客文章列表  
**权限要求**: 需要JWT Token认证

#### 请求头
```
Authorization: Bearer <your-jwt-token>
```

#### 响应示例
```json
[
  {
    "id": 1,
    "title": "我的第一篇博客",
    "content": "这是博客的内容...",
    "author": "张三",
    "createdTime": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "title": "Spring Boot学习笔记",
    "content": "Spring Boot是一个很好的框架...",
    "author": "李四",
    "createdTime": "2024-01-16T14:20:00"
  }
]
```

#### 状态码
- `200`: 请求成功
- `401`: Token无效或未提供
- `500`: 服务器内部错误

---

### 2. 获取单个博客

**接口地址**: `GET /blog/{id}`  
**功能描述**: 根据ID获取指定的博客文章  
**权限要求**: 需要JWT Token认证

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 博客文章ID |

#### 请求头
```
Authorization: Bearer <your-jwt-token>
```

#### 响应示例
```json
{
  "id": 1,
  "title": "我的第一篇博客",
  "content": "这是博客的详细内容...",
  "author": "张三",
  "createdTime": "2024-01-15T10:30:00"
}
```

#### 状态码
- `200`: 请求成功
- `401`: Token无效或未提供
- `404`: 博客不存在
- `500`: 服务器内部错误

---

### 3. 添加博客

**接口地址**: `POST /blog/add`  
**功能描述**: 添加新的博客文章  
**权限要求**: 需要JWT Token认证

#### 请求头
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

#### 请求参数
```json
{
  "title": "博客标题",
  "content": "博客内容",
  "author": "作者名称"
}
```

> **注意**: `id` 和 `createdTime` 字段由系统自动生成，无需在请求中提供

#### 响应示例
```json
"添加成功"
```
或
```json
"添加失败"
```

#### 状态码
- `200`: 请求成功
- `401`: Token无效或未提供
- `400`: 请求参数错误
- `500`: 服务器内部错误

---

### 4. 删除博客

**接口地址**: `DELETE /blog/delete/{id}`  
**功能描述**: 删除指定的博客文章  
**权限要求**: 需要JWT Token认证

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 博客文章ID |

#### 请求头
```
Authorization: Bearer <your-jwt-token>
```

#### 响应示例
```json
"删除成功"
```
或
```json
"删除失败"
```

#### 状态码
- `200`: 请求成功
- `401`: Token无效或未提供
- `404`: 博客不存在
- `500`: 服务器内部错误

---

## 数据模型

### User 用户实体
```json
{
  "id": "Long - 用户ID，自增主键",
  "username": "String - 用户名",
  "password": "String - 密码"
}
```

### Blog 博客实体
```json
{
  "id": "Long - 博客ID，自增主键",
  "title": "String - 博客标题",
  "content": "String - 博客内容",
  "author": "String - 作者名称",
  "createdTime": "LocalDateTime - 创建时间"
}
```

## 错误处理

### 常见错误码
- `401 Unauthorized`: Token无效、过期或未提供
- `400 Bad Request`: 请求参数格式错误
- `404 Not Found`: 请求的资源不存在
- `500 Internal Server Error`: 服务器内部错误

### 错误响应示例
```json
{
  "error": "Unauthorized",
  "message": "未授权或Token无效",
  "timestamp": "2024-01-15T10:30:00"
}
```

## 使用示例

### 完整的请求流程

1. **用户注册**
```bash
curl -X POST http://localhost:8080/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

2. **用户登录**
```bash
curl -X POST http://localhost:8080/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

3. **获取博客列表**
```bash
curl -X GET http://localhost:8080/blog/list \
  -H "Authorization: Bearer <your-jwt-token>"
```

4. **添加博客**
```bash
curl -X POST http://localhost:8080/blog/add \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"title":"新博客","content":"博客内容","author":"testuser"}'
```

## 开发注意事项

1. **Token管理**: 客户端需要妥善保存JWT Token，并在每次请求时携带
2. **安全性**: 生产环境中建议使用HTTPS协议
3. **数据验证**: 建议在客户端和服务端都进行数据格式验证
4. **错误处理**: 客户端应正确处理各种HTTP状态码和错误响应
5. **日志记录**: 建议记录关键操作的日志以便问题排查

## 版本信息

- **API版本**: v1.0
- **最后更新**: 2024-01-15
- **维护者**: 开发团队

---

> 📝 **文档说明**: 本文档基于当前代码结构生成，如有接口变更请及时更新文档。
