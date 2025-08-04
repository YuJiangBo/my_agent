package com.yu.my_agent.service;

import com.yu.my_agent.entity.postgresql.DocumentVector;
import com.yu.my_agent.mapper.postgresql.DocumentVectorMapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// VectorDocumentService.java
@Service
public class VectorDocumentService {
    
    @Autowired
    private DocumentVectorMapper documentVectorMapper;
    
    @Autowired
    private PgVectorStore pgVectorStore; // Spring AI的向量存储
    
    /**
     * 保存文档向量
     */
    public DocumentVector saveDocument(String content, float[] vector, String metadata) {
        DocumentVector doc = new DocumentVector();
        doc.setContent(content);
        doc.setContentVector(vector);
        doc.setMetadata(metadata);
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        
        documentVectorMapper.insert(doc);
        return doc;
    }
    
    /**
     * 向量相似性搜索
     */
    public List<DocumentVector> searchSimilarDocuments(float[] queryVector, int limit) {
        return documentVectorMapper.searchSimilarDocuments(queryVector, limit);
    }
    
    /**
     * 使用Spring AI进行向量搜索
     */
    public List<Document> searchSimilarDocumentsWithAI(String query) {
        return pgVectorStore.similaritySearch(query);
    }
}