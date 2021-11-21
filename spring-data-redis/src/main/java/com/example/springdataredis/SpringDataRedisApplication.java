package com.example.springdataredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis的发布订阅功能
 * 使用 Spring Data Redis 发布和订阅使用 Redis 发送的消息的过程
 * 使用 Spring Data Redis 作为发布消息的方式听起来可能很奇怪，
 * 但是，正如您会发现的，Redis 不仅提供了 NoSQL 数据存储，还提供了消息传递系统。
 * 注册监听器并发送消息
 * Spring Data Redis 提供了使用 Redis 发送和接收消息所需的所有组件。具体需要配置：
 *
 * 连接工厂 使用 Spring Boot 的 default RedisConnectionFactory   LettuceConnectionFactory
 *
 * 一个消息监听器容器
 *
 * Redis 模板
 *
 * 您将使用 Redis 模板发送消息，并将Receiver向消息侦听器容器注册，以便它接收消息。
 * 连接工厂驱动模板和消息侦听器容器，让它们连接到 Redis 服务器。
 *
 */
@SpringBootApplication
public class SpringDataRedisApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataRedisApplication.class);

    /**
     * 消息监听容器
     * @param connectionFactory 连接工厂
     * @param listenerAdapter 消息监听器适配器
     * @return RedisMessageListenerContainer
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        添加监听器，并设置监听匹配chat频道
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

        return container;
    }

    /**
     * 消息监听适配器 将这个POJO成适配成监听器
     * @param receiver 消息接收器
     * @return MessageListenerAdapter
     */
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 消息接收器
     * @return Receiver
     */
    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    /**
     * redis操作模板
     * @param connectionFactory 连接工厂
     * @return StringRedisTemplate
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringDataRedisApplication.class, args);
        StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
        Receiver receiver = ctx.getBean(Receiver.class);

        while (receiver.getCount() == 0) {
            LOGGER.info("Sending message...");
            template.convertAndSend("chat", "Hello from Redis!");
            //noinspection BusyWait
            Thread.sleep(500L);
        }

        System.exit(0);
    }

}
