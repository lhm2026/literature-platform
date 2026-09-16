package com.literature.common.constant;

public class MqConstant {
    // 文献解析主队列
    public static final String LITERATURE_PARSE_QUEUE = "literature.parse.queue";
    public static final String LITERATURE_PARSE_EXCHANGE = "literature.parse.exchange";
    public static final String LITERATURE_PARSE_ROUTING_KEY = "literature.parse";

    // 死信队列（失败任务重试）
    public static final String LITERATURE_DLX_QUEUE = "literature.dlx.queue";
    public static final String LITERATURE_DLX_EXCHANGE = "literature.dlx.exchange";
    public static final String LITERATURE_DLX_ROUTING_KEY = "literature.dlx";
}
