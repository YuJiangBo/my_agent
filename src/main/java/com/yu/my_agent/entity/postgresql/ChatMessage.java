package com.yu.my_agent.entity.postgresql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

// ChatMessage.java - 聊天记录实体
@Data
@TableName("my_agent.chat_messages")
public class ChatMessage {
    @TableId(type = IdType.ASSIGN_ID) // 使用雪花算法
    private Long id;
    
    @TableField("session_id")
    private String sessionId;
    
    @TableField("message_type")
    private String messageType; // USER/ASSISTANT/SYSTEM
    
    @TableField("content")
    private String content;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
}