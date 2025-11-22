package com.family.diary.api;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author Richard Zhang
 * @since 2025-07-23
 */
@Slf4j
@EnableAsync
@SpringBootApplication
@MapperScan("com.family.diary.infrastructure.dao")
public class Application {

    /**
     * 启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("昊昊和爸爸妈妈的小屋日记：后台启动成功！");
    }
}
