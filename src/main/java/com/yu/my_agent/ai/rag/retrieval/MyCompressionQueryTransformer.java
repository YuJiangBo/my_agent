package com.yu.my_agent.ai.rag.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.util.PromptAssert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 余江波 yjb@wupodata.com
 * @since 2025/7/31
 */
public class MyCompressionQueryTransformer implements QueryTransformer {
    
    private static final Logger logger = LoggerFactory.getLogger(MyCompressionQueryTransformer.class);

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate(
            """
            给定以下对话历史记录和后续查询，您的任务是进行合成一个简洁、独立的查询，合并了历史记录中的上下文。确保独立查询清晰、具体，并维护用户的意图。
            
            对话历史:
            {history}
            
            后续的查询:
            {query}
            
            独立的查询:
			""");

    private final ChatClient chatClient;

    private final PromptTemplate promptTemplate;

    public MyCompressionQueryTransformer(ChatClient.Builder chatClientBuilder, @Nullable PromptTemplate promptTemplate) {
        Assert.notNull(chatClientBuilder, "chatClientBuilder cannot be null");

        this.chatClient = chatClientBuilder.build();
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;

        PromptAssert.templateHasRequiredPlaceholders(this.promptTemplate, "history", "query");
    }

    @Override
    public Query transform(Query query) {
        Assert.notNull(query, "query cannot be null");

        logger.debug("Compressing conversation history and follow-up query into a standalone query");

        var compressedQueryText = this.chatClient.prompt()
                .user(user -> user.text(this.promptTemplate.getTemplate())
                        .param("history", formatConversationHistory(query.history()))
                        .param("query", query.text()))
                .options(ChatOptions.builder().temperature(0.0).build())
                .call()
                .content();


        if (!StringUtils.hasText(compressedQueryText)) {
            logger.warn("Query compression result is null/empty. Returning the input query unchanged.");
            return query;
        }

        return query.mutate().text(compressedQueryText).build();
    }

    private String formatConversationHistory(List<Message> history) {
        if (history.isEmpty()) {
            return "";
        }

        return history.stream()
                .filter(message -> message.getMessageType().equals(MessageType.USER)
                        || message.getMessageType().equals(MessageType.ASSISTANT))
                .map(message -> "%s: %s".formatted(message.getMessageType(), message.getText()))
                .collect(Collectors.joining("\n"));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ChatClient.Builder chatClientBuilder;

        @Nullable
        private PromptTemplate promptTemplate;

        private Builder() {
        }

        public Builder chatClientBuilder(ChatClient.Builder chatClientBuilder) {
            this.chatClientBuilder = chatClientBuilder;
            return this;
        }

        public Builder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public CompressionQueryTransformer build() {
            return new CompressionQueryTransformer(this.chatClientBuilder, this.promptTemplate);
        }

    }
}
