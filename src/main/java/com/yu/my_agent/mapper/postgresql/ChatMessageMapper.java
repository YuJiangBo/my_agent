package com.yu.my_agent.mapper.postgresql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.my_agent.entity.postgresql.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// ChatMessageMapper.java
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    
    // 根据会话ID查询消息
    List<ChatMessage> findBySessionId(@Param("sessionId") String sessionId);
    
    // 查询最近的消息
    List<ChatMessage> findLatestBySessionId(@Param("sessionId") String sessionId,
                                           @Param("limit") int limit);
    
    // 统计会话消息数量
    int countBySessionId(@Param("sessionId") String sessionId);

    void insertChatMessage(ChatMessage chatMessage);
}