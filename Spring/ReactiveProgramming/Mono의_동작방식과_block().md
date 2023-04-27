# Mono

## just

> Code
> 

```java
@RestController
public static class MyController{
    @GetMapping("/")
    Mono<String> hello(){
        return Mono.just("Hello WebFlux").log();
    }
}
```

> 결과
> 

```
INFO 31539 --- [ctor-http-nio-2] reactor.Mono.Just.1 : | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription) //  스프링이 알아서 구독함
INFO 31539 --- [ctor-http-nio-2] reactor.Mono.Just.1 : | request(unbounded)
INFO 31539 --- [ctor-http-nio-2] reactor.Mono.Just.1 : | onNext(Hello WebFlux)
INFO 31539 --- [ctor-http-nio-2] reactor.Mono.Just.1 : | onComplete() // 
```

- subcribe를 만들지 않았는데 spring이 만들어줌
- request :
- onNext : Publisher 타고 데이터가 넘어감

```java
@RestController
public static class MyController{
    @GetMapping("/")
    Mono<String> hello(
        log.info("pos1");
        // static factory method : 완성되어 있는 값을 거기에 넣겟다.
        Mono m = Mono.just("Hello WebFlux")
                .doOnNext(c -> log.info(c))
                .log();
        log.info("pos2");
        return m;
    }
}
```

```
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : pos1
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : pos2
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onSubscribe([Fuseable] FluxPeekFuseable.PeekFuseableSubscriber)
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | request(unbounded)
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : Hello WebFlux
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onNext(Hello WebFlux)
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onComplete()
```

- just 는 즉시 수행되어서 사실상 동기지만 구독하는 시점에서 실행되기 때문에 비동기.
- Publisher들은 Subscriber가 Subscribe해야 사용가능하고, 데이터들이 수행되고 실행된다.
