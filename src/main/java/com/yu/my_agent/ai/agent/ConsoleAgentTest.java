// ConsoleAgentTest.java
package com.yu.my_agent.ai.agent;

import com.yu.my_agent.ai.agent.YuManus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Slf4j
public class ConsoleAgentTest implements CommandLineRunner {

    @Autowired
    private YuManus yuManus;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== YuManus 交互式代理控制台测试 ===");
        System.out.println("输入 'quit' 退出程序");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("\n请输入您的问题: ");
            String input = scanner.nextLine();
            
            if ("quit".equalsIgnoreCase(input.trim())) {
                System.out.println("程序退出");
                break;
            }
            
            if (input.trim().isEmpty()) {
                System.out.println("请输入有效的问题");
                continue;
            }
            
            try {
                System.out.println("\n=== 开始处理 ===");
                String result = yuManus.run(input);
                System.out.println("\n=== 处理结果 ===");
                System.out.println(result);
            } catch (Exception e) {
                System.err.println("处理过程中发生错误: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
}
