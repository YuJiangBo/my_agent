package com.yu.my_agent_mcp_server.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ImageSearchServiceTest {

    @Resource
    private ImageSearchService imageSearchService;

    @Test
    void searchImage() {
        String result = imageSearchService.searchImage("computer");
        Assertions.assertNotNull(result);
    }
}
