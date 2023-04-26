# _ReactiveProgramming
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
