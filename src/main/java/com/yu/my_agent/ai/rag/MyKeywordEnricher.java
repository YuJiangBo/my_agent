package com.yu.my_agent.ai.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Transformer组件
 * 自动补充元信息：
 * 在LoveAppVectorStoreConfig中注入当前的Bean，然后在加载文档和添加VectorStore存储之前调用自主切分方法
 *         @Resource
 *         private MyKeywordEnricher MyKeywordEnricher;
 *
 *          // 加载文档
 *         List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
 *         // 自动补充关键词元信息
 *         List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
 *         simpleVectorStore.add(enrichedDocuments);
 */
@Component
class MyKeywordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;

    List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.dashscopeChatModel, 5);
        return enricher.apply(documents);
    }
}

