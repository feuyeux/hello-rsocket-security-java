package org.feuyeux.rsocket.ultimate;

import com.alibaba.fastjson.JSON;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.feuyeux.rsocket.pojo.HelloRequest;
import org.feuyeux.rsocket.pojo.HelloRequests;
import org.feuyeux.rsocket.pojo.HelloResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HelloRSocket extends AbstractRSocket {

    private final List<String> HELLO_LIST = Arrays.asList("Hello", "Bonjour", "Hola", "こんにちは", "Ciao", "안녕하세요");

    private static Publisher<String> apply(List<String> list) {
        return Flux.fromIterable(list);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        String metadata = payload.getMetadataUtf8();
        log.info(">> [MetadataPush]:{}", metadata);
        return Mono.empty();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        HelloRequest helloRequest = JSON.parseObject(payload.getDataUtf8(), HelloRequest.class);
        log.info(">> [FireAndForget] FNF:{}", helloRequest);
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        HelloRequest helloRequest = JSON.parseObject(payload.getDataUtf8(), HelloRequest.class);
        log.info(" >> [Request-Response] data:{}", helloRequest);
        String id = helloRequest.getId();
        HelloResponse helloResponse = getHello(id);
        return Mono.just(DefaultPayload.create(JSON.toJSONString(helloResponse)));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        HelloRequests helloRequests = JSON.parseObject(payload.getDataUtf8(), HelloRequests.class);
        log.info(">> [Request-Stream] data:{}", helloRequests);
        List<String> ids = helloRequests.getIds();
        return Flux.fromIterable(ids)
                .delayElements(Duration.ofMillis(500))
                .map(id -> {
                    HelloResponse helloResponse = getHello(id);
                    return DefaultPayload.create(JSON.toJSONString(helloResponse));
                });
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        final Scheduler scheduler = Schedulers.parallel();

        return Flux.from(payloads)
                .doOnNext(payload -> {
                    log.info(">> [Request-Channel] data:{}", payload.getDataUtf8());
                })
                .map(payload -> {
                    HelloRequests helloRequests = JSON.parseObject(payload.getDataUtf8(), HelloRequests.class);
                    return helloRequests.getIds();
                })
                .flatMap(HelloRSocket::apply)
                .map(id -> {
                    HelloResponse helloResponse = getHello(id);
                    return DefaultPayload.create(JSON.toJSONString(helloResponse));
                })
                .subscribeOn(scheduler);
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