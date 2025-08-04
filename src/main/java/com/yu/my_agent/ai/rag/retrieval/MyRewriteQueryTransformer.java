package com.yu.my_agent.ai.rag.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.util.PromptAssert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author 余江波 yjb@wupodata.com
 * @since 2025/7/31
 */
public class MyRewriteQueryTransformer implements QueryTransformer {


    private static final Logger logger = LoggerFactory.getLogger(MyRewriteQueryTransformer.class);

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
            给定一个用户查询，重写它以在查询{target}时提供更好的结果。删除任何不相关的信息，并确保查询是简洁和具体的。

			原始查询:
			{query}

			改写出的查询:
			""");

    private static final String DEFAULT_TARGET = "vector store";

    private final ChatClient chatClient;

    private final PromptTemplate promptTemplate;

    private final String targetSearchSystem;

    public MyRewriteQueryTransformer(ChatClient.Builder chatClientBuilder, @Nullable PromptTemplate promptTemplate,
                                     @Nullable String targetSearchSystem) {
        Assert.notNull(chatClientBuilder, "chatClientBuilder cannot be null");

        this.chatClient = chatClientBuilder.build();
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;
        this.targetSearchSystem = targetSearchSystem != null ? targetSearchSystem : DEFAULT_TARGET;

        PromptAssert.templateHasRequiredPlaceholders(this.promptTemplate, "target", "query");
    }

    @Override
    public Query transform(Query query) {
        Assert.notNull(query, "query cannot be null");

        logger.debug("Rewriting query to optimize for querying a {}.", this.targetSearchSystem);

        var rewrittenQueryText = this.chatClient.prompt()
                .user(user -> user.text(this.promptTemplate.getTemplate())
                        .param("target", this.targetSearchSystem)
                        .param("query", query.text()))
                .options(ChatOptions.builder().temperature(0.0).build())
                .call()
                .content();

        if (!StringUtils.hasText(rewrittenQueryText)) {
            logger.warn("Query rewrite result is null/empty. Returning the input query unchanged.");
            return query;
        }

        return query.mutate().text(rewrittenQueryText).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ChatClient.Builder chatClientBuilder;

        @Nullable
        private PromptTemplate promptTemplate;

        @Nullable
        private String targetSearchSystem;

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

        public Builder targetSearchSystem(String targetSearchSystem) {
            this.targetSearchSystem = targetSearchSystem;
            return this;
        }

        public MyRewriteQueryTransformer build() {
            return new MyRewriteQueryTransformer(this.chatClientBuilder, this.promptTemplate, this.targetSearchSystem);
        }

    }
}

