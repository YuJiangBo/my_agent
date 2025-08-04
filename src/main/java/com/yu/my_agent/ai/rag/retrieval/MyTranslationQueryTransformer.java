package com.yu.my_agent.ai.rag.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.util.PromptAssert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author 余江波 yjb@wupodata.com
 * @since 2025/7/31
 */
public class MyTranslationQueryTransformer implements QueryTransformer {

    private static final Logger logger = LoggerFactory.getLogger(MyTranslationQueryTransformer.class);

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
            给定一个用户查询，将其翻译为{targetLanguage}。
            如果查询已经在{targetLanguage}中，则原样返回。
            如果您不知道查询的语言，则将其原样返回。
            不要添加解释或任何其他文本。

			原始查询 : {query}

			翻译后的查询 query:
			""");

    private final ChatClient chatClient;

    private final PromptTemplate promptTemplate;

    private final String targetLanguage;

    public MyTranslationQueryTransformer(ChatClient.Builder chatClientBuilder, @Nullable PromptTemplate promptTemplate,
                                         String targetLanguage) {
        Assert.notNull(chatClientBuilder, "chatClientBuilder cannot be null");
        Assert.hasText(targetLanguage, "targetLanguage cannot be null or empty");

        this.chatClient = chatClientBuilder.build();
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;
        this.targetLanguage = targetLanguage;

        PromptAssert.templateHasRequiredPlaceholders(this.promptTemplate, "targetLanguage", "query");
    }

    @Override
    public Query transform(Query query) {
        Assert.notNull(query, "query cannot be null");

        logger.debug("Translating query to target language: {}", this.targetLanguage);

        var translatedQueryText = this.chatClient.prompt()
                .user(user -> user.text(this.promptTemplate.getTemplate())
                        .param("targetLanguage", this.targetLanguage)
                        .param("query", query.text()))
                .options(ChatOptions.builder().temperature(0.0).build())
                .call()
                .content();

        if (!StringUtils.hasText(translatedQueryText)) {
            logger.warn("Query translation result is null/empty. Returning the input query unchanged.");
            return query;
        }

        return query.mutate().text(translatedQueryText).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ChatClient.Builder chatClientBuilder;

        @Nullable
        private PromptTemplate promptTemplate;

        private String targetLanguage;

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

        public Builder targetLanguage(String targetLanguage) {
            this.targetLanguage = targetLanguage;
            return this;
        }

        public TranslationQueryTransformer build() {
            return new TranslationQueryTransformer(this.chatClientBuilder, this.promptTemplate, this.targetLanguage);
        }

    }

}
