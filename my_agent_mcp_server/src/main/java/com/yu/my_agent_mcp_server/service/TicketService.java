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

/**
 * @author 余江波 yjb@wupodata.com
 * @since 2025/7/31
 */
@Service
@Slf4j
public class TicketService {

    @Tool(name = "火车订票查询")
    public String getTicket(@ToolParam(description = "查询方式: 1 (通过站点名称);2 (通过站点编码)") String search_type,
                            @ToolParam(description = "出发站。如：北京、VNP") String departure_station,
                            @ToolParam(description = "到达站。如：北京、OHH") String arrival_station,
                            @ToolParam(description = "出发日期。如：2025-06-18。仅允许15天内的日期") String date,
                            @ToolParam(description = "车次筛选条件，默认所有。如：G。从以下标志中选取多个条件组合[G(高铁/城际),D(动车),Z(直达特快),T(特快),K(快速),O(其他),F(复兴号),S(智能动车组)]", required = false) String filter,
                            @ToolParam(description = "是否可预定班次，默认：1。1 (仅返回可预定的班次); 2 (所有)", required = false) String enable_booking,
                            @ToolParam(description = "出发时间选择，如：上午。凌晨-[0:00-06:00),上午-[6:00-12:00),下午-[12:00-18:00),晚上-[18:00-24:00)", required = false) String departure_time_range) {

        String apiKey = "4a24925c7e7158fccf3b5f5165a435ee";
        String apiUrl = "https://apis.juhe.cn/fapigw/train/query";

        HashMap<String, String> map = new HashMap<>();
        map.put("key", apiKey);
        map.put("search_type", search_type);
        map.put("departure_station", departure_station);
        map.put("arrival_station", arrival_station);
        map.put("date", date);
        map.put("filter", filter);
        map.put("enable_booking", enable_booking);
        map.put("departure_time_range", departure_time_range);

        URL url = null;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(String.format("%s?%s", apiUrl, params(map)));
            BufferedReader in = new BufferedReader(new InputStreamReader((url.openConnection()).getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.toString();
    }

    public static String params(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // 处理value为null的情况
                    if (value == null) {
                        return key + "=";
                    }

                    try {
                        return key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return key + "=" + value;
                    }
                })
                .collect(Collectors.joining("&"));
    }

}
