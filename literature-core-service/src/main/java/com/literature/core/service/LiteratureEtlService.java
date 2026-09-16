package com.literature.core.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Service
public class LiteratureEtlService {

    private final Tika tika = new Tika();

    // M6版本：5个参数构造器，chunkSize=800，minChunkSizeChars=150
    private final TokenTextSplitter splitter = new TokenTextSplitter(800, 150, 5, 10000, true);

    /**
     * ETL主流程：清洗 + 文本分块
     */
    public List<Document> etlPipeline(String rawText) {
        String cleanText = cleanText(rawText);
        Document originDoc = new Document(cleanText);
        // M6 使用 apply()，入参 List<Document>
        List<Document> chunkDocs = splitter.apply(List.of(originDoc));
        return chunkDocs;
    }

    /**
     * 文本清洗
     */
    private String cleanText(String text) {
        if (text == null) return "";
        // 多个空白符替换成单个空格
        return text.replaceAll("\\s+", " ").trim();
    }

    /**
     * Tika提取本地文件文本，支持PDF、docx、txt
     * @throws IOException
     * @throws TikaException
     */
    public String extractFileText(File file) throws IOException, TikaException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return tika.parseToString(fis);
        }
    }

    /**
     * Tika提取远程url文件文本（给MQ消费者调用）
     */
    public String extractText(String fileUrl, String fileType) throws IOException, TikaException {
        URL url = new URL(fileUrl);
        try (InputStream is = url.openStream()) {
            return tika.parseToString(is);
        }
    }
}
