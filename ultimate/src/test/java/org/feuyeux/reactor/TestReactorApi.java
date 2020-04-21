package org.feuyeux.reactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestReactorApi {

    @SneakyThrows
    @Test
    public void test() {
        String[] letters = "The quick brown fox jumps over a lazy dog".split("");
        Flux<String> publisher = Flux.fromArray(letters);

        Flux<String> flux1 = publisher
                .publishOn(Schedulers.newParallel("X"))
                .filter(s -> {
                    log.debug("filter {}", s);
                    return !s.trim().isEmpty();
                })
                .map(s -> {
                    log.debug("map {}", s);
                    return s.toLowerCase();
                })
                .distinct()
                .sort();

        ParallelFlux<String> flux2 = publisher
                .filter(s -> {
                    log.debug("filter {}", s);
                    return !s.trim().isEmpty();
                })
                .map(s -> {
                    log.debug("map {}", s);
                    return s.toUpperCase();
                })
                .distinct()
                .sort()
                .publishOn(Schedulers.newParallel("Y"))
                .parallel(2);

        CountDownLatch latch = new CountDownLatch(1);
        LettersSubscriber subscriber = new LettersSubscriber(latch);

        Flux<String> flux3 = flux1.publishOn(Schedulers.newParallel("Z"))
                .zipWith(flux2, (s1, s2) -> {
                    log.debug("zipWith: {},{}", s1, s2);
                    return String.format("%s[%d] %s[%d]",
                            s2, (int) s2.charAt(0), s1, (int) s1.charAt(0));
                })
                .limitRate(2)
                .publishOn(Schedulers.newParallel("R"));
        flux3.subscribe(subscriber);

        latch.await();

        StepVerifier.create(flux3)
                .expectNext("A[65] a[97]")
                .thenCancel()
                .verify();
    }

    @Test
    public void testApi() {
        Flux<Integer> f1 = Flux.just(1, 2, 3);
        Flux<String> f2 = Flux.just("A", "B", "C");
        Flux<String> f3 = Flux.just("x", "y", "z");
        Flux<String> combineLatestFlux = Flux.combineLatest(
                f1,
                f2,
                f3,
                (arr) -> String.valueOf(arr[1]) + arr[2] + arr[0]);

        StepVerifier.create(combineLatestFlux)
                .expectNext("Cx3")
                .expectNext("Cy3")
                .expectNext("Cz3")
                .expectComplete()
                .verify();
        combineLatestFlux.subscribe(log::info);
    }

    @Test
    public void testFastProducer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Flux<Integer> publisher = Flux.range(2000, 30);

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(5);
            }

            @SneakyThrows
            @Override
            public void onNext(Integer i) {
                TimeUnit.MILLISECONDS.sleep(200);
                log.info("handle {}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.error("", t);
            }

            @Override
            public void onComplete() {
                log.info("complete");
                latch.countDown();
            }
        };
        publisher.filter(i -> i % 2 == 0).subscribe(subscriber);

        publisher.filter(i -> 2004 <= i && i <= 2020)
                .zipWith(Flux.range(1, 100), (year, serial) -> String.format("%d.%d", serial, year))
                .subscribe(s -> log.info("{}", s));

        publisher.filter(i -> i >= (2030 - 5)).subscribe(subscriber);
        latch.await();
    }

    @Test
    public void testSlowProducer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux<Integer> publisher = Flux.range(2004, 20).delayElements(Duration.ofMillis(200));
        publisher.filter(i -> i % 2 == 0)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("onSubscribe, request(n)=Long.MAX_VALUE");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer i) {
                        log.info("handle {}", i);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("", t);
                    }

                    @Override
                    public void onComplete() {
                        log.info("complete");
                        latch.countDown();
                    }
                });
        latch.await();
    }

    class LettersSubscriber implements Subscriber<String> {
        CountDownLatch latch;

        public LettersSubscriber(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onSubscribe(Subscription s) {
            log.info("request(n)=Long.MAX_VALUE");
            s.request(Long.MAX_VALUE);
        }

        @SneakyThrows
        @Override
        public void onNext(String s) {
            TimeUnit.MILLISECONDS.sleep(100);
            log.info(s);
        }

        @Override
        public void onError(Throwable t) {
            log.error("", t);
        }

        @Override
        public void onComplete() {
            log.info("Complete!");
            latch.countDown();
        }
    }
}
