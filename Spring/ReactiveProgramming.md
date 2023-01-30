# ReactiveProgramming_

## Future
### Future
```
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 future : 비동기적인 결과를 나타내는 것, 일종의 핸들러
 */
@Slf4j
public class FutureEx {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();
        Future<String> future = es.submit(() ->
        {
            Thread.sleep(2000);
            log.debug("Async");
            return "hello";
        });
        System.out.println(future.isDone()); // 비동기 작업이 종료되었느지 확인
        Thread.sleep(2100);
        log.debug("EXIT");
        System.out.println(future.isDone());
        log.debug(future.get()); // future를 통해서 비동기 값을 가져옴. -> 결과가 나올 때까지 호출한 메서드는 blocking한 상태가 됨.
    }
}

```
```
> Task :FutureEx.main()
false
[pool-1-thread-1] DEBUG FutureEx - Async
[main] DEBUG FutureEx - EXIT
true
[main] DEBUG FutureEx - hello
```
### FutureTask
```
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/*
 FutureTask :
 */
@Slf4j
public class FutureEx {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();
        FutureTask<String> futureTask = new FutureTask<String>(() ->
        {
            Thread.sleep(2000);
            log.debug("Async");
            return "hello";
        }) {
            @Override
            protected void done() {
                try {
                    System.out.println(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(futureTask);
        es.shutdown(); // 종료를 하지 않으면 thread가 계속 떠있는 상태임.
    }
}
```
```
> Task :FutureEx.main()
[pool-1-thread-1] DEBUG FutureEx - Async
hello
```

```
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;


/*
 FutureTask :
 */
@Slf4j
public class FutureEx {
    FutureTask<String> futureTask = new FutureTask<String>(() ->
    {
        Thread.sleep(2000);
        log.debug("Async");
        return "hello";
    }) {
        @Override
        protected void done() {
            try {
                System.out.println(get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            // if(true) throw new RuntimeException("Async ERROR!!"); // 2)
            log.info("Async");
            return "Hello";
        },
                s -> System.out.println("Result : " + s),
                e -> System.out.println("Error : " + e.getMessage()));

        es.execute(f);
        es.shutdown(); // 종료를 하지 않으면 thread가 계속 떠있는 상태임.
    }

    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }
}
```
1)
```
> Task :FutureEx.main()
[pool-1-thread-1] INFO FutureEx - Async
Result : Hello
```
2) 예외처리
```
Error : Async ERROR!!
```
## 스프링 비동기 기술
```
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
@Slf4j
public class SpringbootRestapiMarketApplication {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
        try (ConfigurableApplicationContext c = SpringApplication.run(SpringbootRestapiMarketApplication.class, args)) {
        }
    }

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");
            Future<String> res = myService.hello();
            log.info("exit : " + res.isDone());
            log.info("result : " + res.get());
        };
    }

    @Component
    public static class MyService {
        @Async
        public Future<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }
    }
}

```
```
INFO 3016 --- [           main] c.p.s.SpringbootRestapiMarketApplication : run()
INFO 3016 --- [           main] c.p.s.SpringbootRestapiMarketApplication : exit : false
INFO 3016 --- [         task-1] c.p.s.SpringbootRestapiMarketApplication : hello()
INFO 3016 --- [           main] c.p.s.SpringbootRestapiMarketApplication : result : Hello
```
```
2023-01-16 22:30:10.409  INFO 26020 --- [           main] c.p.s.SpringbootRestapiMarketApplication : run()
2023-01-16 22:30:10.461  INFO 26020 --- [         task-1] c.p.s.SpringbootRestapiMarketApplication : hello()
2023-01-16 22:30:10.463  INFO 26020 --- [           main] c.p.s.SpringbootRestapiMarketApplication : EXIT
null
```
```
@Bean
ThreadPoolTaskExecutor tp(){
    ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
    te.setCorePoolSize(10);
    te.setMaxPoolSize(100);
    te.setQueueCapacity(50);
    te.setThreadNamePrefix("myThread");
    te.initialize();
    return te;
}
```
```
INFO 9648 --- [      myThread1] c.p.s.SpringbootRestapiMarketApplication : hello()
```
```
@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
@Slf4j
public class SpringbootRestapiMarketApplication {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
    }

    @Component
    public static class MyService {
        @Async("tp")
        public ListenableFuture<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }
    }

    @RestController
    public static class MyController {
        @GetMapping("/async")
        public String async() throws InterruptedException {
            Thread.sleep(2000);
            return "hello";
        }
        @GetMapping("/callable")
        public Callable<String> callable() throws InterruptedException {
            log.info("callable");
            return () -> {
                log.info("async");
                Thread.sleep(2000);
                return "hello";
            };
        }
    }
}
```
// 간단하게 부하테스트해보기
```
package com.practice.springbootrestapimarket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8080/callable";

        StopWatch main = new StopWatch();

        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread " + idx);

                StopWatch sw = new StopWatch();
                sw.start();

                rt.getForObject(url, String.class);
                sw.stop();
                log.info("Elapsed: " + idx + " -> " + sw.getTotalTimeSeconds());
            });

            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);

            main.stop();
            log.info("TOTAL : {}", main.getTotalTimeSeconds());
        }
    }
}
```
```
package com.practice.springbootrestapimarket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
@Slf4j
public class SpringbootRestapiMarketApplication {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
    }

    @Component
    public static class MyService {
        @Async("tp")
        public ListenableFuture<String> hello() throws InterruptedException {
            log.info("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }
    }

    @RestController
    public static class MyController {
        Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

        @GetMapping("/dr")
        public DeferredResult<String> callable() throws InterruptedException {
            log.info("dr");
            DeferredResult<String> dr = new DeferredResult<>(60000L);
            return dr;
        }

        @GetMapping("/dr/count")
        public String drCount() {
            return String.valueOf(results.size());
        }

        @GetMapping("/dr/event")
        public String drEvent(String msg) {
            for (DeferredResult<String> dr : results) {
                dr.setResult("hello " + msg);
                results.remove(dr);
            }
            return "OL";
        }

        @GetMapping("/emmitter")
        public ResponseBodyEmitter emitter() throws InterruptedException {
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(() -> {
                for (int i = 0; i < 50; i++) {
                    try {
                        emitter.send("<p>Stream " + i + " <p>");
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return emitter;
        }
    }
}
```
## 비동기 RestTemplate와 비동기 MVC/Serlvet
```
package com.practice.springbootrestapimarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootRestapiMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
    }

    @RestController
    public static class MyController {

        @Autowired MyService myService;

        RestTemplate rt = new RestTemplate();
        AsyncRestTemplate art = new AsyncRestTemplate();
        AsyncRestTemplate artN = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

        @GetMapping("/rest")
        public String rest(int idx) { // blocking object
            String res = rt.getForObject("http://localhost:8080/rest?idx={idx}", String.class, "hello" + idx);
            return res;
        }

        @GetMapping("/async-rest")
        public ListenableFuture<ResponseEntity<String>> asyncRest(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨
            return art.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx);
        }

        @GetMapping("/async-rest")
        public ListenableFuture<ResponseEntity<String>> asyncRestWithNetty(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨
            return artN.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx);
        }

        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestAndResult(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨

            DeferredResult<String> dr = new DeferredResult<>(); // object를 만들어서 controller가 리턴하면 언제일진 모르지만 언젠가 사용하면 요청에 대한 응답으로 사용하겠다는 것

            ListenableFuture<ResponseEntity<String>> f1 = artN.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx);
            f1.addCallback(s -> { // s = ListenableFuture<ResponseEntity<String>>의 응답값
                dr.setResult(s.getBody() + "/work");
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }

        // 의존성이 존재하여 순차적으로 존재하는 경우
        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestCase1(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨

            DeferredResult<String> dr = new DeferredResult<>();

            ListenableFuture<ResponseEntity<String>> f1 = artN.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx);

            f1.addCallback(s -> { // s = ListenableFuture<ResponseEntity<String>>의 응답값
                ListenableFuture<ResponseEntity<String>> f2 = artN.getForEntity("http://localhost:8080/service2?idx={idx}", String.class, "hello" + idx);
                f2.addCallback(s2 -> {
                    dr.setResult(s2.getBody());
                }, e -> {
                    dr.setErrorResult(e.getMessage());
                });
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }
    }


    @Service
    public static class MyService{
        public ListenableFuture<String> work(String req){
            return new AsyncResult<>(req + "/asyncwork");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor myThreadPool(){
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(10);
        te.initialize();
        return te;
    }
}

```

```
package com.practice.springbootrestapimarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@SpringBootApplication
public class RemoteService {

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8081");
        System.setProperty("server.tomcat.max-thread", "1000");
        SpringApplication.run(RemoteService.class, args);
    }

    @RestController
    public static class MyController {

        @GetMapping("/service")
        public String service(String req) throws InterruptedException {
            Thread.sleep(2000);
            return req + "/service";
        }

        @GetMapping("/service2")
        public String service2(String req) throws InterruptedException {
            Thread.sleep(2000);
            return req + "/service2";
        }
    }
}
```

```
package com.practice.springbootrestapimarket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LoadTest {
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8080/rest?idx={idx}";

        CyclicBarrier barrier = new CyclicBarrier(101); // thread의 동기화를 위해 사용

        StopWatch main = new StopWatch();
        main.start();

        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);

                barrier.await();

                log.info("Thread " + idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String res = rt.getForObject(url, String.class);

                sw.stop();
                log.info("Elapsed: {} {} / {}" , idx , sw.getTotalTimeSeconds(), res);

                return null; // Runnable은 리턴값이 없어서 예외처리를 못함, callable로 만들기 위해서 아무 값이나 리턴해서 사용.
            });


            barrier.await();
            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);
            main.stop();

            log.info("TOTAL : {}", main.getTotalTimeSeconds());
        }
    }
}

## 코드 리팩토링
```
@EnableJpaAuditing
@SpringBootApplication
public class SpringbootRestapiMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestapiMarketApplication.class, args);
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

        AsyncRestTemplate artN = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestCase1(int idx) {

            DeferredResult<String> dr = new DeferredResult<>();

            Completion
                    .from(artN.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx))
                    .andApply(s -> artN.getForEntity("http://", String.class, s.getBody()))
                    .andError(e -> dr.setErrorResult(e))
                    .andAccept(s -> dr.setResult(s.getBody()));


            return dr;
        }
    }

    public static class Completion<S, T> {

        Completion next;
//        Consumer<ResponseEntity<String>> con;
//        Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn;

        public Completion() {
        }

        public Completion(Consumer<ResponseEntity<String>> con) {
            this.con = con;
        }

        public Completion(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
            this.fn = fn;
        }

        public static <S, T> Completion from(ListenableFuture<ResponseEntity<String>> lf) {
            Completion c = new Completion();

            lf.addCallback(s -> {
                c.complete(s);
            }, e -> {
                c.error(e);
            });

            return c;
        }

         void error(Throwable e) {
        }

         void complete(ResponseEntity<String> s) {
            if (next != null) this.next.run(s);
        }

        void run(ResponseEntity<String> s) {
            if (con != null) con.accept(s);
        }

        public void andAccept(Consumer<ResponseEntity<String>> con) {
            Completion c = new Completion(con);
            this.next = c;
        }

        public Completion andApply(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
            Completion c = new Completion(con);
            this.next = c;
            return c;
        }

        public Completion andError(Consumer<Throwable> econ){
            Completion c = new ErrorCompletion(econ);
            this.next = c;
            return c;
        }
    }

    public static class AcceptCompletion extends Completion{
        Consumer<ResponseEntity<String>> con;

        public AcceptCompletion(Consumer<ResponseEntity<String>> con) {
            super(con);
        }

        @Override
        void run(ResponseEntity<String> s) {
            
        }
    }

    public static class ErrorCompletion extends Completion{
        public Consumer<Throwable> econ;

        public ErrorCompletion(Consumer<Throwable> econ) {
            this.econ = econ;
        }

        @Override
        void error(Throwable e) {
            super.error(e);
        }
    }

    @Service
    public static class MyService {
        public ListenableFuture<String> work(String req) {
            return new AsyncResult<>(req + "/asyncwork");
        }
    }
}

```
```
