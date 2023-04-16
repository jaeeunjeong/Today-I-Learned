# AsyncRestTemplet의 콜백 헬과 중복

> 여러 프로세스를 거치게 되어 콜백이 계속해서 발생하면 코드의 중복이 발생하고 가독성이 떨어지는 현상이 발생하기에  
> 명령형 프로그래밍을 함수형으로 변경해

## 과정

- Completion : 콜백 실패에 대한 재정의를 하기 위해 클래스를 정의함
    - 비의존적인 비동기 : from() , 여기서 콜백을 시작함. *일종의 팩토리 메서드*
        
        ```java
        public static <S, T> Completion<S, T> from(ListenableFuture<T> lf) {
            Completion c = new Completion();
        
            lf.addCallback(s -> {
                c.complete(s);
            }, e -> c.error(e));
        
            return c;
        }
        ```
        
    - 의존적인 비동기 : 다음 거로 이어서 할 수 있도록 사슬 구조로 만들기 : 인스턴스 메서드 구조
        - completion을 추가하기 위한 용도, 리턴이 필요하지 않은 경우
        
        ```java
        public void andAccept(Consumer<ResponseEntity<String>> con) {
            Completion c = new Completion(con);
          this.next = c;
        }
        ```
        
    - 리턴도 필요한 경우 andApply
        - 다음(next)로 연결해주고 다음 값을 리턴해준다.
        
        ```java
        public <V> Completion<T, V> andApply(Function<S, ListenableFuture<V>> fn) {
            Completion c = new ApplyCompletion(fn);
            this.next = c;
            return c;
        }
        ```
        
    - Competion next : 레퍼런스 변수로 다음 것과 이어주기 위한 변수
    - 의존적인 비동기로 다음에 넘겨줄 때 : complete
        
        ```java
        public void complete(T s) {
            if (next != null) this.next.run(s);
        }
        ```
        
    - 결과를 리턴받고 다음에 넘겨줘야함 run
        
        ```java
        public void run(ResponseEntity<String> s) {
            if (con != null) con.accept(s);
        		else if(fn != null){
        			ListenableFuture<ResponseEntity<String>> lf = fn.apply(value);
        			lf.addCallback(s -> complete(s), e -> error(e));
        		}
        }
        ```
        
    - **run 메소드의 문제점**
        - **분기처리가 되어있고 각각이 리턴하는 바가 달라**서 문제임. → applyCompliton의 object로 분리해서 **다형성있게 리팩터링**
        
        ```java
        public static class AcceptCompletion<S> extends Completion<S, Void> {
          Consumer<S> con;
        
          public AcceptCompletion(Consumer<S> con) {
              this.con = con;
          }
        
          @Override
          void run(S s) {
              con.accept(s);
          }
        }
        
        public static class ApplyCompletion<S, T> extends Completion<S, T> {
          public Function<S, ListenableFuture<T>> fn;
        
          public ApplyCompletion(Function<S, ListenableFuture<T>> fn) {
              this.fn = fn;
          }
        
          @Override
          void run(S value) {
              ListenableFuture<T> lf = fn.apply(value);
              lf.addCallback(s -> complete(s), e -> error(e));
          }
        }
        ```
        
    - 에러 처리의 중복
        - 전체에서 딱 한번만 처리하도록 변경해보기
            
            ```java
            public static class ErrorCompletion<T> extends Completion<T, T> {
                public Consumer<Throwable> econ;
            		
            		// 실패했을 떄 에러를 받아서 어떻게 처리할지를 정의하기 위함임. 
                public ErrorCompletion(Consumer<Throwable> econ) {
                    this.econ = econ;
                }
            
                @Override
                void error(Throwable e) {
                    super.error(e);
                }
            
            		// next를 처리하지 않고 계속 뒤로 넘기기.
                @Override
                void run(T value) {
                    if (next != null) next.run(value);
                }
            }
            
            ```
            
- 최종 코드

```java
import io.netty.channel.nio.NioEventLoopGroup;
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

import java.util.function.Consumer;
import java.util.function.Function;

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

        AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

        @GetMapping("/async-rest")
        public DeferredResult<String> asyncRestCase1(int idx) {

            DeferredResult<String> dr = new DeferredResult<>();

            String URL1 = null;
            String URL2 = null;

            Completion
                    .from(art.getForEntity(URL1, String.class, "hello" + idx))
                    .andApply(s -> art.getForEntity(URL2, String.class, s.getBody()))
                    .andApply(s -> myService.work(s.getBody))
                    .andError(e -> dr.setErrorResult(e.toString()))
                    .andAccept(s -> dr.setResult(s));
            return dr;
        }
    }

    public static class Completion<S, T> {
        Completion next;

        public Completion() {
        }

        public static <S, T> Completion<S, T> from(ListenableFuture<T> lf) {
            Completion c = new Completion();

            lf.addCallback(s -> {
                c.complete(s);
            }, e -> c.error(e));

            return c;
        }

        void error(Throwable e) {
        }

        void complete(T s) {
            if (next != null) this.next.run(s);
        }

        void run(S value) {
        }

        public void andAccept(Consumer<T> con) {
            Completion<T, Void> c = new AcceptCompletion<>(con);
            this.next = c;
        }

        public <V> Completion<T, V> andApply(Function<S, ListenableFuture<V>> fn) {
            Completion c = new ApplyCompletion(fn);
            this.next = c;
            return c;
        }

        public Completion<T, T> andError(Consumer<Throwable> econ) {
            Completion<T, T> c = new ErrorCompletion(econ);
            this.next = c;
            return c;
        }
    }

    public static class AcceptCompletion<S> extends Completion<S, Void> {
        Consumer<S> con;

        public AcceptCompletion(Consumer<S> con) {
            this.con = con;
        }

        @Override
        void run(S s) {
            con.accept(s);
        }
    }

    public static class ApplyCompletion<S, T> extends Completion<S, T> {
        public Function<S, ListenableFuture<T>> fn;

        public ApplyCompletion(Function<S, ListenableFuture<T>> fn) {
            this.fn = fn;
        }

        @Override
        void run(S value) {
            ListenableFuture<T> lf = fn.apply(value);
            lf.addCallback(s -> complete(s), e -> error(e));
        }
    }

    public static class ErrorCompletion<T> extends Completion<T, T> {
        public Consumer<Throwable> econ;

        public ErrorCompletion(Consumer<Throwable> econ) {
            this.econ = econ;
        }

        @Override
        void error(Throwable e) {
            super.error(e);
        }

        @Override
        void run(T value) {
            if (next != null) next.run(value);
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

## 그 외

- Funtion Interface의 compose/andThen 메서드 : function을 다음거로 이어서 할 수 있는 구조
- TODO 배치에서 예외처리나 연결연결하는 부분 꼼꼼히 봐보기
