//package com.yu.my_agent_mcp_server.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
//
//import java.io.IOException;
//
//@Slf4j
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public void handleException(Exception ex) {
//        if (ex instanceof IOException) {
//            log.warn("Client connection closed: {}", ex.getMessage());
//        } else if (ex instanceof AsyncRequestTimeoutException) {
//            log.warn("Async request timeout occurred");
//        } else if (ex.getMessage() != null && ex.getMessage().contains("isBlocking()")) {
//            log.warn("SSE connection error: {}", ex.getMessage());
//        } else {
//            log.error("Unexpected error occurred", ex);
//        }
//    }
//}