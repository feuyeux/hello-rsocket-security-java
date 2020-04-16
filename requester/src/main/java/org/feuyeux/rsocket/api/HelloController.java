package org.feuyeux.rsocket.api;

import lombok.extern.slf4j.Slf4j;
import org.feuyeux.rsocket.pojo.HelloRequests;
import org.feuyeux.rsocket.pojo.HelloResponse;
import org.feuyeux.rsocket.utils.HelloUtils;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author feuyeux@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("api")
public class HelloController {
    private final HelloRSocketAdapter helloRSocketAdapter;

    HelloController(HelloRSocketAdapter helloRSocketAdapter) {
        this.helloRSocketAdapter = helloRSocketAdapter;
    }

    @GetMapping("hello-forget")
    Mono<Void> fireAndForget() {
        return helloRSocketAdapter.fireAndForget("JAVA");
    }

    @GetMapping("hello/{id}")
    Mono<HelloResponse> getHello(@PathVariable String id) {
        return helloRSocketAdapter.getHello(id);
    }

    @GetMapping(value = "hello-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<HelloResponse> getHellos() {
        List<String> ids = HelloUtils.getRandomIds(5);
        log.info("random={}", ids);
        return helloRSocketAdapter.getHellos(ids);
    }

    @GetMapping(value = "hello-channel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<List<HelloResponse>> getHelloChannel() {
        Flux<HelloRequests> map = Flux.just(
                new HelloRequests(HelloUtils.getRandomIds(3)),
                new HelloRequests(HelloUtils.getRandomIds(3)),
                new HelloRequests(HelloUtils.getRandomIds(3)));
        return helloRSocketAdapter.getHelloChannel(map);
    }
}
