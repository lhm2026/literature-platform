package com.literature.core.consumer;

import com.literature.common.constant.MqConstant;
import com.literature.common.dto.LiteratureParseMsg;
import com.literature.common.enums.LiteratureStatus;
import com.literature.core.service.LiteratureDbService;
import com.literature.core.service.LiteratureEtlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import com.rabbitmq.client.Channel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiteratureParseConsumer {

    private final LiteratureEtlService etlService;
    private final LiteratureDbService literatureDbService;
    private final VectorStore vectorStore;
    private final StringRedisTemplate redisTemplate;

    private static final String IDEM_KEY_PREFIX = "literature:parse:idempotent:";

    @RabbitListener(queues = MqConstant.LITERATURE_PARSE_QUEUE)
    public void consume(LiteratureParseMsg msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String litId = msg.getLiteratureId();
        String idemKey = IDEM_KEY_PREFIX + litId;

        // Redis幂等判断：已经处理过，直接ACK，不再重复解析
        Boolean exist = redisTemplate.hasKey(idemKey);
        if (Boolean.TRUE.equals(exist)) {
            log.info("文献{}已完成解析，幂等拦截，直接ACK", litId);
            channel.basicAck(tag, false);
            return;
        }

        try {
            // 状态机：修改为解析中
            literatureDbService.updateStatus(litId, LiteratureStatus.PARSING);
            log.info("开始解析文献:{}，文件地址:{}", litId, msg.getFileUrl());

            // ETL提取文本
            String rawText = etlService.extractText(msg.getFileUrl(), msg.getFileType());

            // ETL清洗+分块
            List<Document> chunks = etlService.etlPipeline(rawText);
            // 写入向量库Milvus
            vectorStore.add(chunks);

            // 更新状态：解析成功
            literatureDbService.updateStatus(litId, LiteratureStatus.SUCCESS);
            // Redis写入幂等标记，24小时过期
            redisTemplate.opsForValue().set(idemKey, "1", 24, TimeUnit.HOURS);

            log.info("文献{}解析完成，分块数量:{}", litId, chunks.size());
            // 手动ACK，确认消费完成
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 替换printStackTrace，使用日志输出
            log.error("文献{}解析失败", litId, e);
            // 解析失败，更新数据库状态为失败
            literatureDbService.updateStatus(litId, LiteratureStatus.FAIL);
            // Nack，reject=false 投递到死信队列，不再重试
            channel.basicNack(tag, false, false);
        }
    }
}
