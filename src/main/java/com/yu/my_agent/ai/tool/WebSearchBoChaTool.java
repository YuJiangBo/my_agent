package com.yu.my_agent.ai.tool;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 搜索引擎API工具类 - 集成请求、解析和结果处理
 */
@Slf4j
@Data
public class WebSearchBoChaTool {

    // 配置参数
    private final String apiUrl;
    private final String apiKey;

    // 内部状态
    private int timeout = 5000; // 默认5秒超时
    private static final ObjectMapper objectMapper = createObjectMapper();

    public WebSearchBoChaTool(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    // 创建一个ObjectMapper对象
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 设置在反序列化时，如果遇到未知属性，不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        return mapper;
    }

    /**
     * 执行搜索请求
     *
     * @param query 搜索关键词
     * @return 格式化后的搜索结果列表
     */
    @Tool(description = "Search for information from Web Search Engine")
    public String searchInBoCha(@ToolParam(description = "Search query keyword") String query) {
        // 1. 构建请求体
        SearchRequest request = new SearchRequest()
                .setQuery(query)
                .setFreshness("noLimit")
                .setSummary(true)
                .setCount(50);

        // 2. 发送HTTP请求
        HttpResponse response = executeHttpRequest(request);

        // 3. 解析API响应
        ApiResponse apiResponse = parseApiResponse(response);

        // 4. 提取结果并格式化
        return formatResultsAsString(apiResponse);
    }

    private HttpResponse executeHttpRequest(SearchRequest request) {
        String jsonBody = JSONUtil.toJsonStr(request);

        HttpResponse response = HttpRequest.post(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .timeout(timeout)
                .execute();

        if (!response.isOk()) {
            throw new RuntimeException("API请求失败: HTTP " + response.getStatus());
        }

        return response;
    }

    private ApiResponse parseApiResponse(HttpResponse response) {
        try {
            return objectMapper.readValue(response.body(), ApiResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("API响应解析失败", e);
        }
    }

    /**
     * 将结果格式化为带序号的字符串
     */
    private String formatResultsAsString(ApiResponse apiResponse) {
        // 验证业务状态码
        if (apiResponse.getCode() != 200) {
            throw new RuntimeException("API业务错误: " +
                    Optional.ofNullable(apiResponse.getMsg()).orElse("未知错误"));
        }

        // 安全获取结果列表
        List<ApiResponse.ResponseData.WebPages.WebPage> pages = Optional.ofNullable(apiResponse.getData())
                .map(ApiResponse.ResponseData::getWebPages)
                .map(ApiResponse.ResponseData.WebPages::getValue)
                .orElse(Collections.emptyList());

        // 如果没有结果
        if (CollUtil.isEmpty(pages)) {
            return "未找到相关结果";
        }

        // 构建结果字符串
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("共找到 ").append(pages.size()).append(" 条相关结果：\n\n");

        int index = 1;
        for (ApiResponse.ResponseData.WebPages.WebPage page : pages) {
            // 序号
            resultBuilder.append("【").append(index).append("】\n");

            // 标题
            resultBuilder.append("标题: ").append(safeString(page.getName())).append("\n");

            // URL
            resultBuilder.append("链接: ").append(safeString(page.getUrl())).append("\n");

            // 摘要
            resultBuilder.append("摘要: ").append(safeString(page.getSnippet())).append("\n\n");

            index++;
        }

        return resultBuilder.toString();
    }

    private String safeString(String value) {
        return StrUtil.nullToEmpty(value);
    }

    // ================== 内部实体类 ==================

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    private static class SearchRequest {
        private String query;
        private String freshness;
        private boolean summary;
        private int count;

        public SearchRequest setQuery(String query) {
            this.query = query;
            return this;
        }

        public SearchRequest setFreshness(String freshness) {
            this.freshness = freshness;
            return this;
        }

        public SearchRequest setSummary(boolean summary) {
            this.summary = summary;
            return this;
        }

        public SearchRequest setCount(int count) {
            this.count = count;
            return this;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class ApiResponse {
        private int code;
        private String log_id;
        private String msg;
        private ResponseData data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        private static class ResponseData {
            private WebPages webPages;


            @JsonIgnoreProperties(ignoreUnknown = true)
            @Data
            private static class WebPages {
                private List<WebPage> value;

                @JsonIgnoreProperties(ignoreUnknown = true)
                @Data
                private static class WebPage {
                    private String name;
                    private String url;
                    private String snippet;
                    private String siteName;
                    @JsonProperty("dateLastCrawled")
                    private String lastCrawledDate;
                }
            }
        }
    }
}


