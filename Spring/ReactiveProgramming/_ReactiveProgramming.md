# _ReactiveProgramming

## CompletableFuture
```
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CFuture {
public static void main(String[] args) throws ExecutionException, InterruptedException {
    // 1.
//        CompletableFuture<Integer> f = new CompletableFuture<>();
//        f.completeExceptionally(new RuntimeException());
//        System.out.println(f.get());

    // 2.
    ExecutorService es = Executors.newFixedThreadPool(10);
    CompletableFuture
//                .runAsync(() -> log.info("runAsync"))
            .supplyAsync(() -> {
                log.info("runSupplyAsync");
                return 1;
            }) // 2. 1) supply를 구현하고 있어야함. 반드시 리턴!
            .thenApply(s -> {
                log.info("thenApply");
                return s + 1;
            }) // 2. - 2) 앞에서 받은 걸 바탕으로 새로운 작업 진행, -> completableFuture를 리턴함.
            .thenCompose(s1 -> {
                log.info("thenApply");
                return CompletableFuture.completedFuture(s1 + 1); // 2. 4-> thenApply 말고 thenCompose 사용해주면 됨.
            }) // 2. - 2) 앞에서 받은 걸 바탕으로 새로운 작업 진행, -> completableFuture를 리턴함.
            .thenApplyAsync(s -> {
                log.info("thenApply");
                return s + 1;
            }, es) // 뒤에 async가 붙으면 새로운 thread에서 사용할 수 있음 , thread도 꼭 붙여줘야함.
            .exceptionally(e -> -10) // 2. - 5) 에러 발생시 바로 예외처리
            .thenAccept(s2 -> log.info("thenRun {}", s2)) // 2. - 3)결과를 받기만 함.
//                .thenRun(() -> log.info("thenRun")) // thenRun : 이전의 스레드를 이어서 사용할 수 있다. , 의존적으로 사용됨.
//                .thenRun(() -> log.info("thenRun"))
    ;
    log.info("exit");

    ForkJoinPool.commonPool().shutdown();
    ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
}
}

```
```
@GetMapping("/async-rest")
public DeferredResult<String> asyncRestCase1(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨

    DeferredResult<String> dr = new DeferredResult<>();
    toCF(rt.getForEntity("http://"), String.class, "h " + idx)
            .thenCompose(s -> toCF(rt.getForEntity("http://", String.class, s.getBody())))
            .thenCompose(s2 -> toCF(myService.work(s2.getBody))
                    .thenCompose(s3 -> dr.setResult(s3))
                    .exceptionally(e -> {
                        dr.setErrorResult(e.getMessage());
                        return (void) null;
                    }));

    return dr;
}


<T> CompletableFuture<> toCF(ListenableFuture<T> lf) {
    CompletableFuture<T> cf = new CompletableFuture<>();
    lf.addCallback(s -> cf.complete(s), e -> cf.completeExceptionally(e));
    return cf;
}
```
## WebFlux
```
package com.practice.springbootrestapimarket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@RestController
@EnableAsync
@SpringBootApplication
public class SpringbootRestapiMarketApplication {

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "2");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
    }


    public static class MyController {

        static final String URL1 = "http://localhost:8081/service?req={req}";
        static final String URL2 = "http://localhost:8081/service2?req={req}";

        @Autowired
        MyService myService;

        WebClient client = WebClient.create();

        @GetMapping("/rest")
        public Mono<String> rest(int idx) {
            String s = "Hello";
            Mono<String> mono =  Mono.just("hello");

            // 선언만 한 것은 동작하지 않음
            Mono<ClientResponse> response = client.get().url(URL1, idx).exchange();
//            ClientResponse cr = null;
//            Mono<String> body = cr.bodyToMono()

            // 모노에 다시 담아서 하려고 할 때 map 사용
            response.map(clientResponse -> clientResponse.bodyToMono(Strinig.class));

            // flatMap을 이용해서 결과 값 형태를 간결하게 사용 -> 여러개 api 사용하기.
            Mono<String> body = response.flatMap((clientResponse -> clientResponse.bodyToMono(Strinig.class)))
                    .flatMap(res1 -> client.get().uri(URL2, res1).exchange())
                    .flatMap(c-> c.bodyToMono(String.class))
                    .flatMap(res2 -> Mono.fromCompletionStage(myService.work(res2)));

            return body;
        }
    }

    @Service
    public static class MyService{
        @Async // 비동기적으로 사용하고자 할 때 사용가능함. -> 어디서도 blocking 안 걸림!
        public CompletableFuture<String> work(String req){
            return CompletableFuture.completedFuture(req + " /asyncwork");
        }
    }

}
```
## Mono의 동작방식과 block()

@Slf4j
@SpringBootApplication
public class Demo13Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo13Application.class, args);
    }

    @RestController
    public class MyController {
        @GetMapping("/")
        Mono<String> hello(){
            log.info("post1");
            Mono<String> m = Mono.fromSupplier(new Supplier<String>() {
                @Override
                public String get() {
                    return generateHello();
                }
            }).doOnNext(c -> log.info(c)).log();
            String msg2 = m.block(); // mono 형태를 String으로 바꾸고자 할 때 사용.
            log.info("post2, {}", msg2);
            return m;
        }
    }
    private String generateHello(){
        log.info("method generateHello()");
        return "Hello Mono";
    }
}
