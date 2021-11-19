package com.example.restfulweb.controller;

import com.example.restfulweb.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户资源控制器
 */
@RestController
public class UserController {

    public static final String template = "Hello %s";

    private final AtomicInteger counter = new AtomicInteger();

    /**
     * restful web
     * 因为Jackson 2在类路径上，所以MappingJackson2HttpMessageConverter会把User自动转换为json
     * @param name 参数name
     * @return User
     */
    @GetMapping("/user")
    public User user(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User(counter.incrementAndGet(), String.format(template, name));
    }

}
