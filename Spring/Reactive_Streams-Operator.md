# Reactive Streams - Operators
publisher : 데이터를 발생시킴.
- subscribe 메서드를 통해 데이터를 받겠다고 요청함
- subscriber : 데이터를 받는 것
- subscription : 둘 사이에 실제 구독이 일어나는 액션을 담고있음.
  - cancle : 외부로부터 더 이상 구독이 필요하지 않을 경우 등 더이상 필요하지 않을 때 사용함.

## operator
  - map : 데이터를 가공해서 새로운 데이터를 만들어서 넘김
  - sum : 데이터를 계산만 하고 있다가 총 합계를 구한다음에 넘긴다.
  - reduce : 초기의 데이터를 가지고 어떤 연산을 하고 그 값을 연쇄적으로 계산하여 넘기는 것.
  - buffer : 위에서 주는 값을 다 보관하고 있다가 마지막에 collection으로 만드는 것.
```
@Slf4j
public class PubSub {
    public static void main(String[] args) {
        Publisher<Integer> pub = getIterPub();
        pub.subscribe(logSub()); 

        // 데이터 가공하는 pub
        // pub -> mapPub -> sub
        Publisher<Integer> mapPub = mapPub(pub, (Function<Integer, Integer>) s -> s * 10);
        Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
        map2Pub.subscribe(logSub()); // mapPub을 주시하도록 logSub이 구독함.

        // 데이터를 모아서 새로운 데이터를 만드는 pub
        Publisher<Integer> sumPub = sumPub(pub);
        sumPub.subscribe(logSub());

        // 함수형 인터페이스를 이용해서 데이터 새로 만드는 pub 구현
        Publisher<Integer> reducePub = reducePub(pub, 0, (BiFunction<Integer, Integer, Integer>) (a, b) -> a + b));
        Publisher<StringBuilder> reducePub = reducePub(pub, new StringBuilder(),(a, b) -> a.append(b+","));
        reducePub.subscribe(logSub());
    }

    /*
     * 함수형 인터페이스를 적용해서 사용 
    */
    private static Publisher<Integer> reducePub(Publisher<Integer> pub, int init,
                                                BiFunction<Integer, Integer, Integer> bf) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    int result = init;

                    @Override
                    public void onNext(Integer i) {
                        result = bf.apply(result, i);
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(result); // 최종 갱신의 느낌...
                        sub.onComplete();
                    }
                });
            }
        };
    }

    /*
     * 결과만 한번에 넘겨야한다.
     * 데이터가 다 넘겨줬다는 것을 알기 위해 onComplete를 사용한다.
    */
    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    int sum = 0;

                    // 필요한 연산을 계속 수행
                    @Override
                    public void onNext(Integer i) {
                        sum += i;
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(sum); // 한번 넘겨줘서 값을 확정하기
                        sub.onComplete();
                    }
                });
            }
        };
    }

    /*
    * 들어온 데이터들을 가공해서 각각의 데이터들을 넘긴다.
    */
    private static <T> Publisher<T> mapPub(Publisher<T> pub, Function<T, T> f) {
        return new Publisher<T>() {
            @Override
            public void subscribe(Subscriber<? super T> sub) {
                // pub -> mapPub -> sub의 구조인데
                pub.subscribe(new DelegateSub<T>(sub) { // pub이 map을 호출 할 수 있도록 연결한 부분인데, 어떠한 기능을 수행하게 하고 sub를 중개해줌.
                    @Override
                    public void onNext(Integer i) {
                        sub.onNext(f.apply(i));
                    }
                });
            }
        };
    }

    /*
     * 로그를 만드는 것
    */
    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE); // 데이터 무제한으로 받으려고 임의로 설정
            }

            @Override
            public void onNext(Integer i) {
                log.debug("onNext:{}", i);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onErrorL:{}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete:");
            }
        };
    }

    /*
    * iterator Pub
    */
    private static Publisher<Integer> getIterPub() {
        return new Publisher<Integer>() {
            Iterable<Integer> iter = Stream.iterate(1, a -> a + 1).limit(10)
                    .collect(Collectors.toList());

            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iter.forEach(s -> sub.onNext(s));
                            sub.onComplete(); // 완료하면 꼭 날려줘야함.
                        } catch (Throwable t) {
                            sub.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }

    private static class DelegateSub implements Subscriber<Integer> {
        private final Subscriber<? super Integer> sub;

        public DelegateSub(Subscriber sub) {
            this.sub = sub;
        }

        @Override
        public void onSubscribe(Subscription s) {
            sub.onSubscribe(s);
        }

        @Override
        public void onNext(Integer integer) {
            sub.onNext(integer);
        }

        @Override
        public void onError(Throwable t) {
            sub.onError(t);
        }

        @Override
        public void onComplete() {
            sub.onComplete();
        }
    }
}
```
#### pub들 generic으로 변환하기
```
// 가장 기본적인 형태 : 데이터 가공해서 그대로 보여주기, T타입이 들어와서 R타입이 적용되는 형태임
private static <T, R> Publisher<T> mapPub(Publisher<T> pub, Function<T, R> f) {
    return new Publisher<T>() {
        @Override
        public void subscribe(Subscriber<? super T> sub) {
            pub.subscribe(new DelegateSub<T, R>(sub) {
                @Override
                public void onNext(T i) {
                    sub.onNext(f.apply(i));
                }
            });
        }
    };
}


private static class DelegateSub<T, R> implements Subscriber<T> {
    private final Subscriber<? super T> sub;

    public DelegateSub(Subscriber sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(T i) {
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}


private static <T> Subscriber<T> logSub() {
    return new Subscriber<T>() {
        @Override
        public void onSubscribe(Subscription s) {
            log.debug("onSubscribe");
            s.request(Long.MAX_VALUE); // 데이터 무제한으로 받으려고 임의로 설정
        }

        @Override
        public void onNext(T i) {
            log.debug("onNext:{}", i);
        }

        @Override
        public void onError(Throwable t) {
            log.debug("onErrorL:{}", t);
        }

        @Override
        public void onComplete() {
            log.debug("onComplete:");
        }
    };
}

// 함수형 인터페이스 이용해서 데이터 가공하기
private static <T, R> Publisher<R> reducePub(Publisher<T> pub,R init, BiFunction<R, T, R> f) {
    return new Publisher<R>() {
        @Override
        public void subscribe(Subscriber<? super R> sub) {
            pub.subscribe(new DelegateSub<T, R>(sub) {
                R result = init;
                @Override
                public void onNext(T i) {
                    result = bf.apply(result, i);
                }

                @Override
                public void onComplete() {
                    sub.onNext(result);
                    sub.onComplete();
                }
            });
        }
    };
}
```
## Reactor
### 코드
```
import reactor.core.publisher.Flux;

public class ReactorEx {
    public static void main(String[] args) {
        Flux.<Integer>create(e -> { // publisher를 만들고
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
        .log() // 데이터가 어떻게 불러오게 되는지 확인 가능
        .map(s -> s * 10)
        .log() 
        .subscribe(System.out::println); // 실행하도록 구독함...
    }
}
```
### 결과
```
> Task :ReactorEx.main()
[main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
[main] INFO reactor.Flux.Create.1 - onSubscribe(FluxCreate.BufferAsyncSink)
[main] INFO reactor.Flux.Map.2 - onSubscribe(FluxMap.MapSubscriber)
[main] INFO reactor.Flux.Map.2 - request(unbounded)
[main] INFO reactor.Flux.Create.1 - request(unbounded)
[main] INFO reactor.Flux.Create.1 - onNext(1)
[main] INFO reactor.Flux.Map.2 - onNext(10)
10
[main] INFO reactor.Flux.Create.1 - onNext(2)
[main] INFO reactor.Flux.Map.2 - onNext(20)
20
[main] INFO reactor.Flux.Create.1 - onNext(3)
[main] INFO reactor.Flux.Map.2 - onNext(30)
30
[main] INFO reactor.Flux.Create.1 - onComplete()
[main] INFO reactor.Flux.Map.2 - onComplete()
```
- Flux 
  - Reactor가 제공하는 일종의 Publisher
### 스프링으로 확인해보기
- publisher 만 만들면 스프링이 subscriber를 만들어서 제공해준다.
- publisher를 리턴하면 되는데 스프링 mvc가 그 기능을 다 해준다.
```
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ReacitveApplication {
    public static void main(String[] args) {
        SpringBootApplication.run(ReacitveApplication.class, args);
    }

    @RestController
    public static class controller {
        @RequestMapping("/hello")
        public Publisher<String> hello(String name) {
            return new Publisher<String>() {
                @Override
                public void subscribe(Subscriber<? super String> sub) {
                    sub.onSubscribe(new Subscription() {
                        @Override
                        public void request(long n) {
                            sub.onNext("hello" + name);
                            sub.onComplete();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            };
        }
    }
}
```
