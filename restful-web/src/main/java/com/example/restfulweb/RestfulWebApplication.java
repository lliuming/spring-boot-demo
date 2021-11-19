package com.example.restfulweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * restful web 入门demo
 *
 * 此应用程序使用Jackson JSON库自动将类型实例编组Greeting为 JSON。默认情况下，Web Starter 包含 Jackson。
 */
@SpringBootApplication
public class RestfulWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestfulWebApplication.class, args);
    }



}
