package com.yu.my_agent.controller;

import com.yu.my_agent.entity.postgresql.ChatMessage;
import com.yu.my_agent.entity.postgresql.dto.SaveMessageRequest;
import com.yu.my_agent.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ChatController.java
@RestController
@RequestMapping("/chat")
public class ChatController {
    
    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/doChat")
    public ResponseEntity<String> doChat(@RequestParam("message") String message, @RequestParam("sessionId") String sessionId) {
        String content = chatMessageService.doChat(message, sessionId);
        return ResponseEntity.ok(content);
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessage> saveMessage(
            @RequestBody SaveMessageRequest request) {
        ChatMessage message = chatMessageService.saveMessage(
            request.getSessionId(),
            request.getMessageType(),
            request.getContent()
        );
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getMessageHistory(
            @PathVariable String sessionId) {
        List<ChatMessage> history = chatMessageService.getMessageHistory(sessionId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/latest/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getLatestMessages(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "10") int limit) {
        List<ChatMessage> messages = chatMessageService.getLatestMessages(sessionId, limit);
        return ResponseEntity.ok(messages);
    }
}