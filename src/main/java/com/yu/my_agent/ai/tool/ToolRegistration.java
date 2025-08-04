package com.yu.my_agent.ai.tool;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中的工具注册类
 */
@Configuration
public class ToolRegistration{

    @Value("${search-api.api_key_search_url}")
    private String searchApiUrl;

    @Value("${search-api.api_key_search_api}")
    private String searchApiKey;

    @Value("${search-api.api_key_bocha_url}")
    private String searchApiUrl2;

    @Value("${search-api.api_key_bocha_api}")
    private String searchApiKey2;

    @Bean
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey, searchApiUrl);
//        WebSearchBoChaTool webSearchBoChaTool = new WebSearchBoChaTool(searchApiUrl2,searchApiKey2);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
//                webSearchBoChaTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool,
                terminateTool
        );
    }

}
