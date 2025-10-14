package com.key.blogsimplify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户实体，映射到 user 表。
 * <p>
 * 注意：当前示例直接存储明文密码，仅用于教学演示，实际项目必须进行加密存储。
 */
@Data
@TableName("user")
public class User {

    /** 主键，自增 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录用户名。 */
    private String username;

    /** 登录密码（演示用明文）。 */
    private String password;
}
