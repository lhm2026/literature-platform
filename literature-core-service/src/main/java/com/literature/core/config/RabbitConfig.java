package com.literature.core.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 业务队列名称
    public static final String PARSE_QUEUE = "literature.parse.queue";
    public static final String PARSE_EXCHANGE = "literature.parse.exchange";
    public static final String PARSE_ROUTING_KEY = "literature.parse";

    // 死信队列
    public static final String PARSE_DLQ_QUEUE = "literature.parse.dlq.queue";
    public static final String PARSE_DLQ_EXCHANGE = "literature.parse.dlq.exchange";

    // 1. 业务队列，绑定死信交换机
    @Bean
    public Queue parseQueue() {
        return QueueBuilder.durable(PARSE_QUEUE)
                .withArgument("x-dead-letter-exchange", PARSE_DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PARSE_DLQ_QUEUE)
                .build();
    }

    // 2. 业务交换机
    @Bean
    public DirectExchange parseExchange() {
        return ExchangeBuilder.directExchange(PARSE_EXCHANGE).durable(true).build();
    }

    // 3. 绑定：队列绑定交换机
    @Bean
    public Binding parseBinding() {
        return BindingBuilder.bind(parseQueue()).to(parseExchange()).with(PARSE_ROUTING_KEY);
    }

    // ========== 死信配置 ==========
    @Bean
    public Queue parseDlqQueue() {
        return QueueBuilder.durable(PARSE_DLQ_QUEUE).build();
    }

    @Bean
    public DirectExchange parseDlqExchange() {
        return ExchangeBuilder.directExchange(PARSE_DLQ_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding parseDlqBinding() {
        return BindingBuilder.bind(parseDlqQueue()).to(parseDlqExchange()).with(PARSE_DLQ_QUEUE);
    }
}
