// MapperConfig.java
package com.yu.my_agent.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperScanConfig {
    
    @Configuration
    @MapperScan(basePackages = "com.yu.my_agent.mapper.mysql", 
                sqlSessionFactoryRef = "mysqlSqlSessionFactory")
    static class MySqlMapperConfig {
    }

    @Configuration
    @MapperScan(basePackages = "com.yu.my_agent.mapper.postgresql", 
                sqlSessionFactoryRef = "postgresqlSqlSessionFactory")
    static class PostgreSqlMapperConfig {
    }
}
