package com.yu.my_agent.entity.postgresql.dto;

import lombok.Data;

// SaveMessageRequest.java
@Data
public class SaveMessageRequest {
    private String sessionId;
    private String messageType;
    private String content;
}