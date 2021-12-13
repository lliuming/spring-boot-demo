package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.rabbitmq.config.RabbitMQConfig.DIRECT_EXCHANGE_NAME;
import static com.example.rabbitmq.config.RabbitMQConfig.QUEUE_NAME;

@SpringBootTest
class RabbitmqApplicationTests {

    LocalDateTime localDateTime = null;
    int i = 0;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    消息数量
    private final int MESSAGE_COUNT = 200000;


    @Test
    void contextLoads() {

    }

    @Test
    void test1() {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, "foo.bar.baz", "Hello from RabbitMQ!", new CorrelationData("订单ID" + i));
        }
        try {
            Thread.sleep(10000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, "foo.bar.baz", "Hello from RabbitMQ!", new CorrelationData("订单ID2"));
    }

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "simpleRabbitListenerContainerFactory")
    public void receiveMessage1(Message message, Channel channel) {
//        System.out.println("测试者：Received <" + message.toString() + ">");
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        try {
            i++;

            if (i % 2500 == 0 || i == MESSAGE_COUNT) {
//            if (i == MESSAGE_COUNT) {
                System.out.println("----------------------------" + localDateTime.until(LocalDateTime.now(), ChronoUnit.MILLIS));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }
        } catch (IOException e) {
            System.out.println("消息确认失败");
        }
    }

}
