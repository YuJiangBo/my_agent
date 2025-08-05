package com.yu.my_agent_mcp_server.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TicketServiceTest {

    @Resource
    private TicketService getTicket;

    @Test
    void getTicket() {
        String result = getTicket.getTicket("1","重庆","厦门","2025-08-08",null,null,null);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}