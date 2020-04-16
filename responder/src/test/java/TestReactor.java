import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TestReactor {
    private static Publisher<Integer> apply(List<Integer> list) {
        Flux<Integer> integerStream = Flux.fromIterable(list);
        return integerStream;
    }

    @Test
    public void testFlatMapFlux() {
        List<Integer> PrimeNumbers = Arrays.asList(5, 7, 11, 13);
        List<Integer> OddNumbers = Arrays.asList(1, 3, 5);
        List<Integer> EvenNumbers = Arrays.asList(2, 4, 6, 8);
        Flux.just(PrimeNumbers, OddNumbers, EvenNumbers)
                .flatMap(TestReactor::apply)
                .subscribe(i -> log.info("{}", i));
    }

    @Test
    public void testFlatMapList() {
        // Creating a list of Prime Numbers
        List<Integer> PrimeNumbers = Arrays.asList(5, 7, 11, 13);
        // Creating a list of Odd Numbers
        List<Integer> OddNumbers = Arrays.asList(1, 3, 5);
        // Creating a list of Even Numbers
        List<Integer> EvenNumbers = Arrays.asList(2, 4, 6, 8);
        List<List<Integer>> listOfListofInts = Arrays.asList(PrimeNumbers, OddNumbers, EvenNumbers);
        log.info("The Structure before flattening is {}", listOfListofInts);

        // Using flatMap for transformating and flattening.
        Stream<List<Integer>> stream = listOfListofInts.stream();
        List<Integer> listofInts = stream
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
        log.info("The Structure after flattening is {}", listofInts);

        stream = listOfListofInts.stream();
        stream.flatMap(list -> list.stream()).forEach(i -> log.info("{}", i));
    }

    @Test
    public void testFn() {
        Flux.range(1, 10)
                .subscribe(i -> log.info("{}:{}", i, fn(i)));
    }

    int fn(int n) {
        if (n <= 1) {
            return 1;
        }
        return fn(n - 1) + (n - 1);
    }

    @Test
    public void test2() {
        Flux<Integer> f = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5));
        f
                .flatMap(a -> {
                    log.info("Received: {}", a);
                    return Mono.just(a).subscribeOn(Schedulers.parallel());
                })
                .doOnNext(a -> log.info("Received: {}", a))
                .flatMap(a -> {
                    log.info("Received in flatMap: {}", a);
                    return Mono.just(++a).subscribeOn(Schedulers.elastic());
                })
                .subscribe(a -> log.info("Received: {}", a));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void test1() {
        Scheduler scheduler = Schedulers.newParallel("parallel-scheduler", 4);

        Flux<String> source = Flux
                .fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .publishOn(scheduler)
                .map(String::toUpperCase);

        ConnectableFlux<String> co = source.publish();

        source.subscribe(d -> log.info("Subscriber 1: {}", d));
        source.subscribe(d -> log.info("Subscriber 2: {}", d));

        co.connect();
    }

    @Test
    public void test0() {
        Flux<String> flux = Flux.generate(
                () -> 1,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                });
        flux.subscribe(s -> log.info("{}", s));
    }
}
