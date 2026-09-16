# 智能文献检索平台 literature-platform
> 基于SpringCloud + RAG的分布式文献检索系统，支持PDF/Word文档解析、向量入库、智能问答。

## ✨ 项目亮点
- 微服务拆分：存储服务、文档解析服务、核心检索服务
- 文档解析：使用Apache Tika提取PDF、Word文本内容
- RAG检索：Milvus向量数据库做向量存储，SpringAI实现文档向量化与检索
- 消息队列：RabbitMQ异步处理文档解析任务，削峰填谷
- 缓存：Redis缓存热门向量检索结果，冷热分离优化查询性能
- 分布式：Redisson分布式锁控制文档并发解析，防止重复入库

## 🧩 模块说明
- literature-storage-service：文件上传接收、消息生产者
- literature-parse-service：Tika文档解析、文本清洗、消息消费
- literature-core-service：向量生成、Milvus检索、RAG问答接口

## 🛠️ 技术栈
Java 17 | Spring Cloud | SpringAI | Apache Tika | Milvus | RabbitMQ | Redis | Redisson | Maven

## 🚀 快速启动
1. 启动Milvus、Redis、RabbitMQ
2. 依次启动三个微服务
3. 上传文献文件，后台异步解析并向量化
4. 调用检索接口，基于文档内容进行问答

## 📌 项目收获
掌握RAG检索链路设计，微服务异步解耦，向量数据库的工程落地，面试高频项目。
