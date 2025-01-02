package com.dnpa.common.config.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig authenticationDBConfig() {
        return new HikariConfig();
    }

    @Primary
    @Bean("authenticationDB")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.configuration")
    public DataSource authenticationDataSource() {
        return new HikariDataSource(authenticationDBConfig());
    }
}
