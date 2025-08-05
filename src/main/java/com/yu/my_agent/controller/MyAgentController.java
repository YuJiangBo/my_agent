package com.yu.my_agent.controller;

import com.yu.my_agent.ai.agent.YuManus;
import com.yu.my_agent.ai.app.MyAgentApp;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private YuManus yuManus;

    /**
     * 同步调用 AI
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/chat/sync")
    public String doChatWithAgentApp(String message, String chatId) {
        return agentApp.doChat(message, chatId);
    }

    /**
     * 同步调用 AI 人工确认
     *
     * @param message
     * @return
     */
    @GetMapping("/chat/ack")
    public String doChatWithAck(String message) {
        return yuManus.run(message);
    }
}
