package com.yu.my_agent.ai.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        responseAfter(advisedResponse);
        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);
        Flux<AdvisedResponse> advisedResponse = chain.nextAroundStream(advisedRequest);
        return aggregateAdvisedResponse(advisedResponse, this::responseAfter);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public AdvisedRequest before(AdvisedRequest advisedRequest){
        log.info("AI Request: {}", advisedRequest.userText());
        return advisedRequest;
    }

    public void responseAfter(AdvisedResponse advisedResponse){
        log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    public Flux<AdvisedResponse> aggregateAdvisedResponse(Flux<AdvisedResponse> advisedResponses,
                                                          Consumer<AdvisedResponse> aggregationHandler) {

        AtomicReference<Map<String, Object>> adviseContext = new AtomicReference<>(new HashMap<>());

        return new MessageAggregator().aggregate(advisedResponses.map(ar -> {
            adviseContext.get().putAll(ar.adviseContext());
            return ar.response();

        }), aggregatedChatResponse -> {

            AdvisedResponse aggregatedAdvisedResponse = AdvisedResponse.builder()
                    .response(aggregatedChatResponse)
                    .adviseContext(adviseContext.get())
                    .build();

            aggregationHandler.accept(aggregatedAdvisedResponse);

        }).map(cr -> new AdvisedResponse(cr, adviseContext.get()));
    }


}
