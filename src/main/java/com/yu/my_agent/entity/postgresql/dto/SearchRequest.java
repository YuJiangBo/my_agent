package com.yu.my_agent.entity.postgresql.dto;

import lombok.Data;

// SearchRequest.java
@Data
public class SearchRequest {
    private float[] queryVector;
    private int limit = 5;
}