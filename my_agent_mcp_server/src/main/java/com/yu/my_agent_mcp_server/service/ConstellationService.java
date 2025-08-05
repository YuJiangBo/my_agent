package com.yu.my_agent_mcp_server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ConstellationService {

    @Tool(description = "获取指定星座的运势")
    public String getConstellation(@ToolParam(description = "星座名称，如天蝎座、双鱼座") String constellationName,
                               @ToolParam(description = "时间类型，如今日(today)、明日(tomorrow)、本周(week)、本月(month)、今年(year)") String timeType) {
        log.info("调用了星座运势工具");
        String apiKey = "4f179061f5e7b82eb788d7baab98edf5";
        String apiUrl = "http://web.juhe.cn/constellation/getAll";

        Map<String, String> map = new HashMap<>();
        map.put("key", apiKey);
        map.put("consName", constellationName);
        map.put("type", timeType);
        String constellation = null;
        try {
            URL url = new URL(String.format("%s?%s", apiUrl, params(map)));
            BufferedReader in = new BufferedReader(new InputStreamReader((url.openConnection()).getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            constellation = response.toString();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return constellation;
    }

    public static String params(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> {
                    try {
                        return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return entry.getKey() + "=" + entry.getValue();
                    }
                })
                .collect(Collectors.joining("&"));
    }
}