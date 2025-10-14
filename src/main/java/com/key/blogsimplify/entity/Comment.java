package com.key.blogsimplify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体，对应数据库的 comment 表。
 */
@Data
@TableName("comment")
public class Comment {

    /** 评论主键，自增 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属博客主键，用于建立关联。 */
    private Long blogId;

    /** 评论者用户名。 */
    private String userName;

    /** 评论正文内容。 */
    private String content;

    /** 创建时间，由服务端写入。 */
    private LocalDateTime createdTime;
}
