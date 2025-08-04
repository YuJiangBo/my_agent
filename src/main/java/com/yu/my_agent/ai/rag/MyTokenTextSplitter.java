package com.yu.my_agent.ai.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档切片：
 * 在LoveAppVectorStoreConfig中注入当前的Bean，然后在加载文档和添加VectorStore存储之前调用自主切分方法
 *         @Resource
 *         private MyTokenTextSplitter myTokenTextSplitter;
 *
 *         // 加载文档
 *         List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
 *         // 自主切分
 *         List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
 *         simpleVectorStore.add(splitDocuments);
 */
@Component
class MyTokenTextSplitter {
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }

    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(200, 100, 10, 5000, true);
        return splitter.apply(documents);
    }

}
