# Mono
## just

> Code
> 

```java
@RestController
public static class MyController{
    @GetMapping("/")
    Mono<String> hello(
        log.info("pos1");
        Mono m = Mono.just("Hello WebFlux")
                .doOnNext(c -> log.info(c))
                .log();
        log.info("pos2");
        return m;
    }
}
```

> 결과
> 

```
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : pos1
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : pos2
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onSubscribe([Fuseable] FluxPeekFuseable.PeekFuseableSubscriber)
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | request(unbounded)
INFO 31590 --- [ctor-http-nio-2] com.-.Application   : Hello WebFlux
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onNext(Hello WebFlux)
INFO 31590 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onComplete()
```

- subcribe를 만들지 않았는데 spring이 만들어줌
- request(unbounded) : 내부적으로 max 반환함
- onNext : Publisher 타고 데이터가 넘어감
- publishing할 데이터가 준비되어 있는 상태
- just 는 즉시 수행되어서 사실상 동기지만 구독하는 시점에서 실행되기 때문에 비동기.
- Publisher들은 Subscriber가 Subscribe해야 사용가능하고, 데이터들이 수행되고 실행된다.
- doOnNext : 현재 데이터가 파악이 가능하다.

```java
@GetMapping("/")
Mono<String> hello(){
    log.info("pos1");
    String msg = generateHello();
    // static factory method : 완성되어 있는 값을 거기에 넣겟다.
    Mono m = Mono.just(msg)
            .doOnNext(c -> log.info(c))
            .log();
    log.info("pos2");
    return m;
    }
}
```

```
INFO 31619 --- [ctor-http-nio-2] com.example.demo013.Demo013Application   : pos1
INFO 31619 --- [ctor-http-nio-2] com.example.demo013.Demo013Application   : method generateHello()
INFO 31619 --- [ctor-http-nio-2] com.example.demo013.Demo013Application   : pos2
INFO 31619 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onSubscribe([Fuseable] FluxPeekFuseable.PeekFuseableSubscriber)
INFO 31619 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | request(unbounded)
INFO 31619 --- [ctor-http-nio-2] com.-.Application   : Hello Mono
INFO 31619 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onNext(Hello MonoINFO 31619 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onComplete()
```

- 하나 이상의 subscriber를 가질 수 있다.

## publinshing source : cold/hot

- hot
    - 실시간으로 데이터가 들어온다면 앞의 데이터는 받지 않고,
    - 구독하는 시점부터 데이터를 가지고 온다.
- cold
    - 구독을 할 때마다 처음부터 다 가지고 해줌.
    - replay → 여러 번 가능함.

## block

- mono 의 결과값을 수행할 수 있음.
- 한번 더 subscribe 해서 값을 가져온다.
- 값을 가져오기 위해 데이터를 기다리기 때문에 block이라는 의미를 가짐.

```java
@RestController
public class MyController{
    @GetMapping("/")
    Mono<String> hello(){
        log.info("pos1");
        String msg = generateHello();
        // static factory method : 완성되어 있는 값을 거기에 넣겟다.
        Mono<String> m = Mono.fromSupplier(() -> generateHello())
                .doOnNext(c -> log.info(c))
                .log();

        String msg2 = m.block();
        log.info("pos2 - {}", msg2);
        return m;
    }

}

private String generateHello(){
    log.info("method generateHello()");
    return "Hello Mono";
}
public static void main(String[] args) {
    SpringApplication.run(Demo013Application.class, args);
}
```

```
INFO 31716 --- [           main] com.-.Application   : Started Demo013Application in 0.728 seconds (process running for 0.985)
INFO 31716 --- [ctor-http-nio-2] com.-.Application   : pos1
INFO 31716 --- [ctor-http-nio-2] com.-.Application   : method generateHello()
INFO 31716 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onSubscribe([Fuseable] FluxPeekFuseable.PeekFuseableSubscriber)
INFO 31716 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | request(unbounded)
INFO 31716 --- [ctor-http-nio-2] com.-.Application   : method generateHello()
INFO 31716 --- [ctor-http-nio-2] com.-.Application   : Hello Mono
INFO 31716 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onNext(Hello Mono)
INFO 31716 --- [ctor-http-nio-2] reactor.Mono.PeekFuseable.1              : | onComplete()
ERROR 31716 --- [ctor-http-nio-2] a.w.r.e.AbstractErrorWebExceptionHandler : [e9c13ac8-1]  500 Server Error for HTTP GET "/"

java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-2
	at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:83) ~[reactor-core-3.5.5.jar:3.5.5]
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
Error has been observed at the following site(s):
	*__checkpoint ⇢ HTTP GET "/" [ExceptionHandlingWebHandler]
Original Stack Trace:
		at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:83) ~[reactor-core-3.5.5.jar:3.5.5]
		at reactor.core.publisher.Mono.block(Mono.java:1710) ~[reactor-core-3.5.5.jar:3.5.5]
~~
```

**2.0 이후 버전에서 block는 차단으로 처리되어서 에러 발생함.**
