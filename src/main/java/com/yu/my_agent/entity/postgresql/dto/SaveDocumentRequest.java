package com.yu.my_agent.entity.postgresql.dto;

import lombok.Data;

// SaveDocumentRequest.java
@Data
public class SaveDocumentRequest {
    private String content;
    private float[] vector;
    private String metadata;
}
