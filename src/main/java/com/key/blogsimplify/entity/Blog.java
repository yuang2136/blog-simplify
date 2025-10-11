package com.key.blogsimplify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdTime;
}
