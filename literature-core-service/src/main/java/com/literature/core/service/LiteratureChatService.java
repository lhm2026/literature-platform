package com.literature.core.service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class LiteratureChatService {
    private final ChatClient chatClient;

    public LiteratureChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(
                        // MessageChatMemoryAdvisor：保存对话上下文，实现多轮连续问答
                        MessageChatMemoryAdvisor.builder(new InMemoryChatMemory()).build()
                )
                // 递进式Prompt策略
                .defaultSystem("""
                        你是文献智能问答助手，请基于检索出来的文献知识库回答用户问题。
                        回答采用递进式策略，先给出结论，再引用文献片段支撑。
                        如果知识库没有相关信息，禁止编造内容。
                        """)
                .build();
    }

    public String chat(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
