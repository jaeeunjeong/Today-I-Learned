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
package com.practice.springbootrestapimarket;

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
            log.debug("hello()");
            Thread.sleep(2000);
            return new AsyncResult<>("Hello");
        }
    }
}
```
