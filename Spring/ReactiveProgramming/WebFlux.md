# **WebFlux**

## 특징

- Mono/Flux에 데이터를 담아서 리턴해야한다.
- Mono/Flux를 선언만으론 값을 리턴하지 않음
    - Mono/Flux는 Publish를 implement하는 것이기 때문에 구독을 해야 값을 리턴하는데, WebFlux에서 구독의 기능을 제공하여 Mono/Flux 타입으로 리턴하면 해당 값들을 리턴하면 그 때 동작하여 값을 리턴한다.
- 여러 기능을 한꺼번에 쓰고싶다면 **flapMap**을 사용한다.

  ```java
  Mono<ClientResponse> res = webClient.get().uri(URL1, idx).exchange();
  Mono<Mono<String>> map = res.map(ClientResponse -> ClientResponse.bodyToMono(String.class));

  ->
  Mono<ClientResponse> res = webClient.get().uri(URL1, idx).exchange();
  Mono<String> stringMono = res.flatMap(ClientResponse -> ClientResponse.bodyToMono(String.class));
  ```
## 코드
  ```java
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReactiveApplication {

    static final String URL1 = "http://localhost:8081/service?req={req}";
    static final String URL2 = "http://localhost:8081/service2?req={req}";
    @Autowired
    MyService myService;

    public static void main(String[] args) {
        System.setProperty("reactor.netty.ioWorkerCount", "2");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(ReactiveApplication.class, args);
    }


    public static class MyController {
        WebClient webClient = WebClient.create();

        @GetMapping("/rest")
        public Mono<ClientResponse> rest(int idx) {
            String s = "Hello";

            Mono<ClientResponse> res = webClient.get().uri(URL1, idx).exchange()
                    .flatMap(res1 -> webClient.get().uri(URL2, res1).exchange())
                    .flatMap(c -> c.bodyToMono(String.class))
                    .flatMap(res2 -> Mono.fromCompletionStage(myService.work(res2)));

            return res;

        }
    }

    @Service
    public static class MyService {
        public String work(String req) {
            return req + " /asyncwork";
        }
    }
}
```
