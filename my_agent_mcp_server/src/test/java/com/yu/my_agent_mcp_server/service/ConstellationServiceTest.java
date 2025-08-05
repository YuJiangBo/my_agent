package com.yu.my_agent_mcp_server.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ConstellationServiceTest {

    @Resource
    private ConstellationService constellationService;

    @Test
    void getConstellation() {
        String result = constellationService.getConstellation("双鱼座","year");
        System.out.println(result);
        Assertions.assertNotNull(result);
    }

}