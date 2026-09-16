package com.literature.storage.config;

import com.literature.common.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    // ==========新增这一段 RabbitAdmin Bean，放在最上面即可 ==========
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public DirectExchange literatureParseExchange() {
        return ExchangeBuilder.directExchange(MqConstant.LITERATURE_PARSE_EXCHANGE).durable(true).build();
    }

    @Bean
    public DirectExchange literatureDlxExchange() {
        return ExchangeBuilder.directExchange(MqConstant.LITERATURE_DLX_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue literatureParseQueue() {
        return QueueBuilder.durable(MqConstant.LITERATURE_PARSE_QUEUE)
                .withArgument("x-dead-letter-exchange", MqConstant.LITERATURE_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", MqConstant.LITERATURE_DLX_ROUTING_KEY)
                .withArgument("x-message-ttl", 30000)
                .build();
    }

    @Bean
    public Queue literatureDlxQueue() {
        return QueueBuilder.durable(MqConstant.LITERATURE_DLX_QUEUE).build();
    }

    @Bean
    public Binding literatureParseBinding() {
        return BindingBuilder.bind(literatureParseQueue()).to(literatureParseExchange()).with(MqConstant.LITERATURE_PARSE_ROUTING_KEY);
    }

    @Bean
    public Binding literatureDlxBinding() {
        return BindingBuilder.bind(literatureDlxQueue()).to(literatureDlxExchange()).with(MqConstant.LITERATURE_DLX_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter());
        // 生产者确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack){
                // 消息投递失败，可以记录日志/落库告警
                System.err.println("消息投递失败 id:" + correlationData.getId());
            }
        });
        rabbitTemplate.setReturnsCallback(returned -> {
            System.err.println("消息路由失败");
        });
        return rabbitTemplate;
    }
}
