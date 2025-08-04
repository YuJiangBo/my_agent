package com.yu.my_agent.ai.agent.model;

/**
 * 代理执行状态的枚举类  
 */  
public enum AgentState {  
  
    /**  
     * 空闲状态  
     */  
    IDLE,  
  
    /**  
     * 运行中状态  
     */  
    RUNNING,

    /**
     * 等待用户输入状态
     */
    WAITING_FOR_CONFIRMATION,
  
    /**  
     * 已完成状态  
     */  
    FINISHED,  
  
    /**  
     * 错误状态  
     */  
    ERROR  
}
