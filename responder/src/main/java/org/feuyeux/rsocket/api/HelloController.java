package org.feuyeux.rsocket.api;

import lombok.extern.slf4j.Slf4j;
import org.feuyeux.rsocket.pojo.HelloRequest;
import org.feuyeux.rsocket.pojo.HelloRequests;
import org.feuyeux.rsocket.pojo.HelloResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author feuyeux@gmail.com
 */
@Slf4j
@Controller
public class HelloController {

    private final List<String> HELLO_LIST = Arrays.asList("Hello", "Bonjour", "Hola", "こんにちは", "Ciao", "안녕하세요");

    /**
     * REQUEST_FNF -->!
     *
     * @param helloRequest fnf
     * @return void
     */
    @MessageMapping("hello-forget")
    public Mono<Void> fireAndForget(HelloRequest helloRequest) {
        log.info(">> [FireAndForget] FNF:{}", helloRequest.getId());
        return Mono.empty();
    }

    /**
     * REQUEST_RESPONSE request --> <-- response
     *
     * @param helloRequest hello request
     * @return hello response
     */
    @MessageMapping("hello-response")
    Mono<HelloResponse> requestAndResponse(HelloRequest helloRequest) {
        log.info(" >> [Request-Response] data:{}", helloRequest);
        String id = helloRequest.getId();
        return Mono.just(getHello(id));
    }

    /**
     * REQUEST_STREAM request --> <-- <-- stream
     *
     * @param helloRequests hello requests
     * @return hello response flux
     */
    @MessageMapping("hello-stream")
    Flux<HelloResponse> requestStream(HelloRequests helloRequests) {
        log.info(">> [Request-Stream] data:{}", helloRequests);
        List<String> ids = helloRequests.getIds();
        return Flux.fromIterable(ids)
                .delayElements(Duration.ofMillis(500))
                .map(this::getHello);
    }

    /**
     * REQUEST_CHANNEL request channel --> --> <-- --> <--
     *
     * @param requests hello request flux
     * @return hello response flux
     */
    @MessageMapping("hello-channel")
    Flux<List<HelloResponse>> requestChannel(Flux<HelloRequests> requests) {
        return Flux.from(requests)
                .doOnNext(message -> log.info(">> [Request-Channel] data:{}", message))
                .map(message -> message.getIds().stream()
                        .map(this::getHello)
                        .collect(Collectors.toList()));
    }

    private HelloResponse getHello(String id) {
        int index;
        try {
            index = Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            index = 0;
        }
        if (index > 5) {
            return new HelloResponse(id, "你好");
        }
        return new HelloResponse(id, HELLO_LIST.get(index));
    }

    private HelloResponse getHello(int index) {
        return new HelloResponse(String.valueOf(index), HELLO_LIST.get(index));
    }
}