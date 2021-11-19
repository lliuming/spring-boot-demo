package com.example.resttemplate;

import com.example.resttemplate.entity.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateApplication {

//    日志记录器
    public static final Logger log = LoggerFactory.getLogger(RestTemplateApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);
    }

    /**
     * 它使用 Jackson JSON 处理库来处理传入的数据
     * @param builder restTemplate构建器
     * @return restTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * CommandLineRunner 接口的 Component 会在所有 Spring Beans 都初始化之后，SpringApplication.run() 之前执行，非常适合在应用程序启动之初进行一些数据初始化的工作
     * @param restTemplate 访问restful服务器的工具类
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            Quote quote = restTemplate.getForObject(
                    "https://quoters.apps.pcfone.io/api/random", Quote.class);
            assert quote != null;
            log.info(quote.toString());
        };
    }

}
