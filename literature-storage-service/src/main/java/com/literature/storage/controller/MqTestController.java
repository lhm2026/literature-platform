package com.literature.storage.controller;

import com.literature.common.constant.MqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendTestMsg() {
        // 发送测试消息
        rabbitTemplate.convertAndSend(
                MqConstant.LITERATURE_PARSE_EXCHANGE,
                MqConstant.LITERATURE_PARSE_ROUTING_KEY,
                "测试文档解析任务"
        );
        return "✅ 测试消息发送成功！";
    }
}
