package com.yu.my_agent.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class DataSourceConfig {

    @Bean("mysqlDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public HikariDataSource mysqlDataSource() {
        return new HikariDataSource();
    }

}
