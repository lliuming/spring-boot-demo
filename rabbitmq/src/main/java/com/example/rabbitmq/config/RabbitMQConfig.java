package com.example.rabbitmq.config;

import com.example.rabbitmq.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 注册监听器并发送消息
     * Spring AMQP RabbitTemplate提供了使用 RabbitMQ 发送和接收消息所需的一切。但是，您需要：
     * <p>
     * 配置消息侦听器容器。
     * <p>
     * 声明队列、交换和它们之间的绑定。
     * <p>
     * 配置一个组件来发送一些消息来测试监听器。
     * Spring Boot 会自动创建一个连接工厂和一个 RabbitTemplate，从而减少您必须编写的代码量。
     */

    //  队列名称
    public static final String QUEUE_NAME = "spring-boot-queue";

//    集群测试队列
    public static final String CLUSTER_QUEUE_NAME_1 = "cluster-1";

    public static final String CLUSTER_QUEUE_NAME_2 = "cluster-2";

    //  topic交换机名称
    public static final String TOPIC_EXCHANGE_NAME = "spring-boot-topic-exchange";

    //  fanout交换机名称
    public static final String FANOUT_EXCHANGE_NAME = "spring-boot-fanout-exchange";

    //  direct交换机名称
    public static final String DIRECT_EXCHANGE_NAME = "spring-boot-direct-exchange";


    /**
     * 创建一个 AMQP 队列
     * @return Queue
     */
//    @Bean
    public Queue queue() {
        Map<String, Object> options = new HashMap<>();
        //        设置死信交换机，消息超时，或者消息超出队列容量，或者消息被拒绝，而没有被退回到原队列，消息会回到死信交换机
        options.put("x-dead-letter-exchange", TOPIC_EXCHANGE_NAME);
//        当一个消息是死信消息的时候使用的路由键
        options.put("x-dead-letter-routing-key", "死信交换机的路由键");
        return new Queue(QUEUE_NAME, true, false, false, options);
    }

    /**
     * 创建一个 AMQP 队列
     * @return Queue
     */
    @Bean
    public Queue cluster_queue0() {
        Map<String, Object> options = new HashMap<>();
        //        设置死信交换机，消息超时，或者消息超出队列容量，或者消息被拒绝，而没有被退回到原队列，消息会回到死信交换机
        options.put("x-dead-letter-exchange", TOPIC_EXCHANGE_NAME);
//        当一个消息是死信消息的时候使用的路由键
        options.put("x-dead-letter-routing-key", "死信交换机的路由键");
        return new Queue(CLUSTER_QUEUE_NAME_1, true, false, false, options);
    }

    /**
     * 创建一个 AMQP 队列
     * @return Queue
     */
    @Bean
    public Queue cluster_queue1() {
        Map<String, Object> options = new HashMap<>();
        //        设置死信交换机，消息超时，或者消息超出队列容量，或者消息被拒绝，而没有被退回到原队列，消息会回到死信交换机
        options.put("x-dead-letter-exchange", TOPIC_EXCHANGE_NAME);
//        当一个消息是死信消息的时候使用的路由键
        options.put("x-dead-letter-routing-key", "死信交换机的路由键");
        return new Queue(CLUSTER_QUEUE_NAME_2, true, false, false, options);
    }

    /**
     * TopicExchange类型交换机
     * 和direct差不多，不同的是支持模糊匹配
     * @return TopicExchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    /**
     * FanoutExchange类型交换机
     * 这种类型的交换机，routingKey不起作用
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }

    /**
     * DirectExchange类型交换机
     * 1.首先要绑定队列
     * 2.然后匹配路由键
     * 这样就会收到消息
     * @return DirectExchange
     */
    @Bean
    public DirectExchange directExchange() {
        HashMap<String, Object> map = new HashMap<>();
//        设置备用交换机,用FANOUT_EXCHANGE_NAME作为备用交换机，有备用交换机就不会调用失败回调,如果备用交换机也失败了，就会触发失败回调
        map.put("alternate-exchange", FANOUT_EXCHANGE_NAME);
        return new DirectExchange(DIRECT_EXCHANGE_NAME,true,false, map);
    }

    /**
     * 将这两者绑定在一起
     * 定义RabbitTemplate发布到交换时发生的行为
     * #全匹配
     * *word匹配
     * @param cluster_queue0 队列
     * @param topicExchange 交换机
     * @return Binding
     */
    @Bean(name = "topic")
    public Binding binding(Queue cluster_queue0, TopicExchange topicExchange) {
        return BindingBuilder.bind(cluster_queue0).to(topicExchange).with("foo.bar.#");
    }

//    @Bean(name = "direct")
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("foo.bar.baz");
    }

    @Bean(name = "fanout")
    public Binding binding(Queue cluster_queue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cluster_queue1).to(fanoutExchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        发送方确认失败回调
        rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {
//            correlationData   相关参数
//            消息有没有发送成功  b
//            消息发送失败的原因  s
//            System.out.println(correlationData);
//            System.out.println("b -> " + b);
//            System.out.println("s -> " + s);
        });
//        加入队列失败回调
//        开启失败回调
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(System.out::println);
        return rabbitTemplate;
    }

//    /**
//     * 消息适配器
//     * @param receiver 消息接收器
//     * @return MessageListenerAdapter
//     */
//    @Bean
//    public MessageListenerAdapter messageListenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    /**
//     * 消息监听器容器
//     * @param connectionFactory 连接工厂
//     * @param messageListenerAdapter 消息监听器适配器
//     * @return SimpleMessageListenerContainer
//     */
//    @Bean
//    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setQueueNames(QUEUE_NAME);
//        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
//        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
//        return simpleMessageListenerContainer;
//    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory connectionFactory  = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        connectionFactory.setChannelCacheSize(2500);
        return connectionFactory;
    }

    /**
     * 监听器容器工厂
     * @return SimpleRabbitListenerContainerFactory
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
//        设置确认模式为手动确认,手动确认，防止消息消费失败，消息丢失了
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        设置预取值，前提要开启手动确认,最大2500
        simpleRabbitListenerContainerFactory.setPrefetchCount(2500);
        return simpleRabbitListenerContainerFactory;
    }

}
