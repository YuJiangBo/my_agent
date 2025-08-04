package com.yu.my_agent.service;

import com.yu.my_agent.ai.app.MyAgentApp;
import com.yu.my_agent.entity.postgresql.ChatMessage;
import com.yu.my_agent.mapper.postgresql.ChatMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// ChatMessageService.java
@Service
public class ChatMessageService {
    
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private MyAgentApp myAgentApp;
    
    /**
     * 保存聊天消息
     */
    public ChatMessage saveMessage(String sessionId, String messageType, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        
        chatMessageMapper.insert(message);
        return message;
    }
    
    /**
     * 获取会话消息历史
     */
    public List<ChatMessage> getMessageHistory(String sessionId) {
        return chatMessageMapper.findBySessionId(sessionId);
    }
    
    /**
     * 获取最近的聊天记录
     */
    public List<ChatMessage> getLatestMessages(String sessionId, int limit) {
        return chatMessageMapper.findLatestBySessionId(sessionId, limit);
    }
    
    /**
     * 统计会话消息数
     */
    public int countMessages(String sessionId) {
        return chatMessageMapper.countBySessionId(sessionId);
    }

    /**
     * 聊天
     * @param sessionId
     * @param message
     * @return
     */
    public String doChat(String message, String sessionId) {
        String chatResponse = myAgentApp.doChat(message, sessionId);
        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setId(1L);
        chatMessage.setMessageType("USER");
        chatMessage.setContent(message);
        chatMessage.setSessionId(sessionId);
//        chatMessageMapper.insertChatMessage(chatMessage);
        chatMessageMapper.insert(chatMessage);
        return chatResponse;
    }
}