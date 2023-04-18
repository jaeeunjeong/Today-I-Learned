# ComparableFuture

## 특징

- CompletionStage를 interface로 받음
    - 하나의 비동기를 수행하고 저게 완료되었을 때 의존적으로 또다른 작업들을 수행할 수 있도록한다. *공식문서 확인해보기*
    - 모든 값이 완료 될때까지 또는 하나의 값이 완료될때까지 기다릴지 선택할 수 있음.
    - 이 기능때문에 이어서 사용이 가능함.
        - thenRun와 같은 것을 이용해서!
- 다른 Thread로 작동되는지 확인

```java
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CFuture {
    public static void main(String[] args) throws InterruptedException {
				CompletableFuture.runAsync(() -> log.info("runAsync"))
                .thenRun(() -> log.info("relay"));

        log.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
```

결과

```java
[main] INFO com.example.reactive.CFuture -- exit
[ForkJoinPool.commonPool-worker-1] INFO com.example.reactive.CFuture -- runAsync
[ForkJoinPool.commonPool-worker-1] INFO com.example.reactive.CFuture -- relay
```

## CompletableFuture 메서드

```java
public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(10);

        CompletableFuture
                .supplyAsync(() -> {
                    // 2. 1) supply를 구현하고 있어야함. 반드시 리턴!
                    log.info("runSupplyAsync");
                    return 1;
                }, es)
                .thenApply(s -> {
                    log.info("thenApply");
                    return s + 1;
                })
                .thenCompose(s1 -> {
                    // 2. - 2) 앞에서 받은 걸 바탕으로 새로운 작업 진행, -> completableFuture를 리턴함.
                    // 2. 4-> thenApply 말고 thenCompose 사용해주면 됨.
                    log.info("thenCompose");
                    return CompletableFuture.completedFuture(s1 + 1);
                })
                .thenApplyAsync(s2 -> {
                    log.info("thenApplyAsync");
                    return s2 + 1;
                }, es) // 뒤에 async가 붙으면 새로운 thread에서 사용할 수 있음 , thread도 꼭 붙여줘야함.
                .exceptionally(e -> -10) // 2. - 5) 에러 발생시 바로 예외처리
                .thenAccept(s3 ->
                        // 2. - 3) consumer 결과를 받기만 함.
                        log.info("thenAccept {}", s3))
                .exceptionally(e -> {
                    System.out.println("ERROR");
                    return null;
                })
        ;

        log.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }

}
```

```java
[main] INFO com.example.reactive.CFuture -- exit
[pool-1-thread-1] INFO com.example.reactive.CFuture -- thenApply
[pool-1-thread-1] INFO com.example.reactive.CFuture -- thenCompose
[pool-1-thread-2] INFO com.example.reactive.CFuture -- thenApplyAsync
[pool-1-thread-2] INFO com.example.reactive.CFuture -- thenAccept 4
```

## ListenableFuture를 CompletableFuture로 바꾸기

```java
package com.example.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class ReactiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(10);
        te.initialize();
        return te;
    }

    @RestController
    public static class MyController {
        @Autowired
        MyService myService;

        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestCase1(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨

            DeferredResult<String> dr = new DeferredResult<>();
            toCF(rt.getForEntity("http://"), String.class, "h " + idx)
                    .thenCompose(s -> toCF(rt.getForEntity("http://", String.class, s.getBody())))
                    .thenCompose(s2 -> toCF(myService.work(s2.getBody)))
                    .thenCompose(s3 -> dr.setResult((String) s3))
                    .exceptionally(e -> {
                        dr.setErrorResult(e.getMessage());
                        return (Void) null;
                    });

            return dr;
        }
        
		/**
     * ListenableFuture를 ComletableFuture로 변환하기.
     * @param lf
     * @return
     * @param <T>
     */
    <T> CompletableFuture<T> toCF(ListenableFuture<T> lf) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        lf.addCallback(s -> cf.complete(s), e -> cf.completeExceptionally(e));
        return cf;
    }

        @Service
        public static class MyService {

            public ListenableFuture<String> work(String req) {
                return new AsyncResult<>(req + "/asyncwork");
            }
        }
    }
}
```
