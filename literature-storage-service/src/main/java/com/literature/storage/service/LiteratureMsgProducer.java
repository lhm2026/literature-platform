package com.literature.storage.service;
import com.literature.common.constant.MqConstant;
import com.literature.common.dto.LiteratureParseMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiteratureMsgProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendParseMsg(LiteratureParseMsg msg) {
        CorrelationData correlationData = new CorrelationData(msg.getLiteratureId());
        rabbitTemplate.convertAndSend(MqConstant.LITERATURE_PARSE_EXCHANGE,
                MqConstant.LITERATURE_PARSE_ROUTING_KEY, msg, correlationData);
    }
}
