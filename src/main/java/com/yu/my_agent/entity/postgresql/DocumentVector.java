package com.yu.my_agent.entity.postgresql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

// DocumentVector.java - PostgreSQL向量文档实体
@Data
@TableName("document_vectors")
public class DocumentVector {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("content")
    private String content;

    @TableField("content_vector")
    private float[] contentVector;

    @TableField("metadata")
    private String metadata;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}