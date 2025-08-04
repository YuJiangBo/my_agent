package com.yu.my_agent.controller;

import com.yu.my_agent.ai.app.MyAgentApp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 余江波 yjb@wupodata.com
 * @since 2025/8/4
 */
@RestController
@RequestMapping("/agent")
public class MyAgentController {

    @Resource
    private MyAgentApp agentApp;
    /**
     * 同步调用 AI 恋爱大师应用
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/chat/sync")
    public String doChatWithAgentApp(String message, String chatId) {
        return agentApp.doChat(message, chatId);
    }
}
