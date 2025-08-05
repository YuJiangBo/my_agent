//package com.yu.my_agent_mcp_server.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class AsyncConfig implements WebMvcConfigurer {
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        // 设置异步请求超时时间为3分钟
//        configurer.setDefaultTimeout(180000);
//
//        // 创建Spring的线程池任务执行器
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);         // 核心线程数
//        executor.setMaxPoolSize(10);         // 最大线程数
//        executor.setQueueCapacity(25);       // 队列容量
//        executor.setThreadNamePrefix("async-"); // 线程名前缀
//        executor.initialize();
//
//        configurer.setTaskExecutor(executor);
//    }
//}