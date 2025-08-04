package com.yu.my_agent.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

@Slf4j
public class WebScrapingTool {

    @Tool(description = "Scrape the content of a web page")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        log.info("调用网页抓取工具");
        try {
            Document doc = Jsoup.connect(url).get();
            String content = "";
            Elements metas = doc.select("meta");
            if(metas.isEmpty()){
                return "网页抓取内容为空";
            }
            for (Element meta : metas) {
                // 获取name属性为"XXX"的标签
                Element targetDiv = meta.select("[name=description]").first();

                if (targetDiv != null) {
                    // 获取同级的content属性内容
                    content = targetDiv.attr("content");
                    break;
                }
            }
            log.info("网页抓取内容：{}", content);
            return content;
//            return doc.html();
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
