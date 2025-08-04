package com.yu.my_agent.ai.tool;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class WebSearchTool {

    private final String apiKey;
    private final String searchApiUrl;

    public WebSearchTool(String apiKey, String searchApiUrl) {
        this.apiKey = apiKey;
        this.searchApiUrl = searchApiUrl;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        log.info("调用网页搜索工具");
        log.info("调用网页搜索工具参数: {}", query);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");
        try {
            String response = HttpUtil.get(searchApiUrl, paramMap);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
//            JSONArray organicResults = jsonObject.getJSONArray("related_searches");
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            if (organicResults == null || organicResults.isEmpty()) {
                return "No search results found.";
            }
            // 取出返回结果的前5条（如果不足5条则取全部）
            int resultSize = Math.min(organicResults.size(), 5);
            List<Object> objects = organicResults.subList(0, resultSize);
            // 拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));
            return result;
//            // 拼接搜索结果为字符串
//            StringBuilder resultBuilder = new StringBuilder();
//            for (Object obj : objects) {
//                JSONObject result = (JSONObject) obj;
//                String title = result.getStr("title", "");
//                String link = result.getStr("link", "");
//                String snippet = result.getStr("snippet", "");
//                resultBuilder.append("标题: ").append(title)
//                        .append("\n链接: ").append(link)
//                        .append("\n摘要: ").append(snippet)
//                        .append("\n\n");
//            }
//            log.info("使用了search-api工具,result: {}", resultBuilder);
//            return resultBuilder.toString();
        } catch (Exception e) {
            log.error("搜索出错", e);
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
