package com.example.rabbitmq;

import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

import static com.example.rabbitmq.config.RabbitMQConfig.DIRECT_EXCHANGE_NAME;
import static com.example.rabbitmq.config.RabbitMQConfig.TOPIC_EXCHANGE_NAME;

/**
 * rabbitmq发布订阅消息
 */
@SpringBootApplication
public class RabbitmqApplication {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public RabbitmqApplication(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }

//    @Bean
    CommandLineRunner init() {
        return (args) -> {
            System.out.println("Sending message...");
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, "foo.bar.baz", "Hello from RabbitMQ!", new CorrelationData("订单ID"));
//            AbstractConnectionFactory connectionFactory = (AbstractConnectionFactory) rabbitTemplate.getConnectionFactory();
//            connectionFactory.destroy();
            //noinspection ResultOfMethodCallIgnored
            receiver.getCountDownLatch().await(10000, TimeUnit.MILLISECONDS);
        };
    }

}
