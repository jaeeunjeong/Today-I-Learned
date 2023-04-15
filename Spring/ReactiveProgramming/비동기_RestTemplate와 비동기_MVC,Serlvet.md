# 비동기_RestTemplate와 비동기_MVC,Serlvet

- CyclicBarrier : blocking을 만듦.
    - await();

```java
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
public class ReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
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
        public DeferredResult<String> asyncRestAndResult(int idx) { 

						// object를 만들어서 controller가 리턴하면 언제일진 모르지만 언젠가 사용하면 요청에 대한 응답으로 사용하겠다는 것
            DeferredResult<String> dr = new DeferredResult<>(); 

            ListenableFuture<ResponseEntity<String>> f1 = artN.getForEntity("http://localhost:8080/service?idx={idx}", String.class, "hello" + idx);
            f1.addCallback(s -> { // s = ListenableFuture<ResponseEntity<String>>의 응답값
                dr.setResult(s.getBody() + "/work");
            }, e -> {
                dr.setErrorResult(e.getMessage());
            });
            return dr;
        }

        // 의존성이 존재하여 순차적으로 존재하는 경우 중첩해서 사용
        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestCase1(int idx) { 

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

- 다른 서버간 접속을 확인해보기 위해 띄움.

```java
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

- 전송된 쓰레드를 확인하기 위한 클래스

```java
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
```
- 비동기를 통해 적은 쓰레드로도 빠르게 해결할 수 있다.
- 여러 서비스를 거쳐서 호출을 하게된다면 callback을 계속해서 리턴받게 되어 코드도 깔끔하지 못하고 로직이 깔끔해지지 못하는 상황이 발생한다.
- JMC를 통해 사용중인 쓰레드를 확인할 수 있다.
- 클라이언트 쓰레드는 적게/ 워커쓰레드는 많이 해서 빠른 처리가 가능하다. Non Blocking 때문임.
## 문제점 : Callback hell
- 아래의 코드와 같이 callback이 연달아 걸린 것.
- 중복 코드가 많아서 읽기가 불편하고 예외처리를 일일이 해줘야함.
  - method로 분리
```java
// 의존성이 존재하여 순차적으로 존재하는 경우
@GetMapping("/async-relay")
public DeferredResult<String> asyncRestCase1(int idx) { // thread를 백그라운드에서 추가로 만들어서 사용됨

    DeferredResult<String> dr = new DeferredResult<>();

    ListenableFuture<ResponseEntity<String>> f1 = art.getForEntity(URL1, String.class, "hello 1 " + idx);

    f1.addCallback(s -> { // s = ListenableFuture<ResponseEntity<String>>의 응답값
        ListenableFuture<ResponseEntity<String>> f2 = art.getForEntity(URL2, String.class, "hello" 2  + idx);
        f2.addCallback(s2 -> {
            ListenableFuture<ResponseEntity<String>> f3 = art.getForEntity(URL3, String.class, "hello 3" + idx);
            f3.addCallback(s3 -> {
                dr.setResult(s3)
            }, e-> {
                dr.setErrorResult(e.getMessage())
            });
            dr.setResult(s2.getBody());
        }, e -> {
            dr.setErrorResult(e.getMessage());
        });
    }, e -> {
        dr.setErrorResult(e.getMessage());
    });
    return dr;
}
```
