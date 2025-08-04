package com.yu.my_agent.ai.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于内存的向量存储
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LocalMarkdownDocumentLoader localMarkdownDocumentLoader;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
//        // 加载文档
//        List<Document> documents = localMarkdownDocumentLoader.loadMarkdowns();
//        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
