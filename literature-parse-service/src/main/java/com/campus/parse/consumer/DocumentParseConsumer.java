package com.campus.parse.consumer;

import com.literature.common.constant.MqConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentParseConsumer {

    @RabbitListener(queues = MqConstant.LITERATURE_PARSE_QUEUE)
    public void handleParseTask(String message) {
        System.out.println("【parse服务收到任务消息】：" + message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("【文档解析任务处理完成】");
    }
}
