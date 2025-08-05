package com.yu.my_agent_mcp_server.config;


import com.yu.my_agent_mcp_server.service.ConstellationService;
import com.yu.my_agent_mcp_server.service.ImageSearchService;
import com.yu.my_agent_mcp_server.service.TicketService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallBackProviderConfiguration {


    @Bean
    public ToolCallbackProvider tools(ConstellationService constellationService, ImageSearchService imageSearchService, TicketService ticketService){
        return MethodToolCallbackProvider.builder().toolObjects(constellationService,imageSearchService, ticketService).build();
    }

//    @Bean
//    public ToolCallbackProvider myTools(ConstellationService constellationService, ImageSearchService imageSearchService) {
//        ToolCallback[] toolCallbacks = ToolCallbacks.from(constellationService, imageSearchService);
//        List<ToolCallback> tools = Arrays.asList(toolCallbacks);
//        return ToolCallbackProvider.from(tools);
//    }
}
