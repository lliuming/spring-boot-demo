package com.example.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.example.rabbitmq.config.RabbitMQConfig.QUEUE_NAME;

/**
 * 消息接收器
 */
@Component
public class Receiver {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 接收消息
     * @param message 消息字符串
     */
    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
//        countDownLatch.countDown();
    }

    /**
     * 订单消息
     * @param message 消息对象
     */
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "simpleRabbitListenerContainerFactory")
    public void receiveMessage(Message message, Channel channel) {
        System.out.println("Received <" + message.toString() + ">");
        countDownLatch.countDown();
        if (getCountDownLatch().getCount() == 0) {
//            扣除库存成功，开始消息确认，前提是开启了手动确认，AcknowledgeMode.MANUAL
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                System.out.println("下单成功");
            } catch (IOException e) {
                System.out.println("消息确认失败");
            }
        } else {
//            扣除库存失败，消息回退
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);    // 批量退回
                System.out.println("消费者1：消息退回");
            } catch (IOException e) {
                System.out.println("消息退回失败");
            }
//            try {
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);  // 单条退回
//            } catch (IOException e) {
//                System.out.println("消息退回失败");
//            }
        }
    }

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "simpleRabbitListenerContainerFactory")
    public void receiveMessage1(Message message, Channel channel) {
        System.out.println("消费者2：Received <" + message.toString() + ">");
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            System.out.println("消息确认失败");
        }
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

}
