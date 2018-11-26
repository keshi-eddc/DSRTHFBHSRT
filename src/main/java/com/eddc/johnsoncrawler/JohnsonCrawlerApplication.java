package com.eddc.johnsoncrawler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eddc.johnsoncrawler.mapper")
public class JohnsonCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JohnsonCrawlerApplication.class, args);
    }
}
