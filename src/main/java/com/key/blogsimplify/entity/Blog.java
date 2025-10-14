package com.key.blogsimplify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客实体，对应数据库表 blog。
 * 使用 {@link TableName} 与 {@link TableId} 注解描述表名和主键策略，
 * MyBatis-Plus 会自动完成对象与数据表之间的映射。
 */
@Data
@TableName("blog")
public class Blog {

    /** 主键，自增 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 博客标题。 */
    private String title;

    /** 博客正文内容。 */
    private String content;

    /** 作者昵称。 */
    private String author;

    /** 创建时间，由服务端维护。 */
    private LocalDateTime createdTime;
}
