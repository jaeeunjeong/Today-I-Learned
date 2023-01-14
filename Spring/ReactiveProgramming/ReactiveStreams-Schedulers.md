# Reactive Streams - Schedulers
- event의 경우에는 백그라운드에서 발생하는 일이 많음.
- 막연히 대기하는 것의 경우엔 핵심 thread가 계속해서 블로킹 하는 경우는 비효율적
- 스케줄러 방식
  - subscribeOn
  - publishOn
## subscribeOn
  - 하나의 publisher와 subscriber 사이에 operator를 넣어서 사용
  - subscriber의 값을 operator의 스레드에서 사용해달라고 하는 것.
  - `flux.subscribeOn(schedulers.single()).subscribe();`를 이용해서 사용
  - subscribe(처리하는 쪽)은 빠른데, publisher(호출하는 쪽)의 속도가 느린 경우 사용
  - subscribe의 속도를 예측하기 어려운 경우 사용
### 코드
```
@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {

        // publisher
        Publisher<Integer> pub  = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // ## subscribeOn 새로운 thread를 만들어줘서 사용하도록 해보기. ## 
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.execute(() -> pub.subscribe(sub));
        };


        // subscribe
        subOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {} ", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });
        log.debug("EXIT");
    }
}
```
### 결과
```
[main] DEBUG SchedulerEx - EXIT
[pool-1-thread-1] DEBUG SchedulerEx - onSubscribe
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 1 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 2 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 3 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 4 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 5 
[pool-1-thread-1] DEBUG SchedulerEx - onComplete
```
## publishOn
    - subscriber를 받는 쪽에서 별도의 스레드를 생성
    - subscriber가 속도가 느린 경우 사용한다.
    - wrapping하고 있는 걸 만들어야한다.
    - 반드시 single thread임
    - pub하는 곳에 single를 만들어서 사용함
### 코드
```
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {

        // publisher
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    log.debug("request()");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // subscribe
        Publisher<Integer> pubOnPub = sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                ExecutorService es = Executors.newSingleThreadExecutor();

                @Override
                public void onSubscribe(Subscription s) {
                    log.debug("onSubscribe");
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Integer integer) {
                    es.execute(() -> sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.execute(() -> sub.onError(t));
                }

                @Override
                public void onComplete() {
                    es.execute(() -> sub.onComplete());
                }
            });
        };

        // subscribe
        pubOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {} ", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        log.debug("EXIT");
    }
}
```
### 결과
```
[main] DEBUG SchedulerEx - onSubscribe
[main] DEBUG SchedulerEx - request()
[main] DEBUG SchedulerEx - EXIT
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 1 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 2 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 3 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 4 
[pool-1-thread-1] DEBUG SchedulerEx - onNext : 5 
[pool-1-thread-1] DEBUG SchedulerEx - onComplete
```

## subcribeOn, publishOn 같이 사용
### 코드
```
@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {

        // publisher
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    log.debug("request()");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // SubscribeOn
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                public String getThreadNamePrefix() {
                    return "subOn-";
                }
            });
            es.execute(() -> pub.subscribe(sub));
        };

        // PublishOn
        Publisher<Integer> pubOnPub = sub -> {
            subOnPub.subscribe(new Subscriber<Integer>() {
                ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                    @Override
                    public String getThreadNamePrefix() {
                        return "pubOn-";
                    }
                });

                @Override
                public void onSubscribe(Subscription s) {
                    log.debug("onSubscribe");
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Integer integer) {
                    es.execute(() -> sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.execute(() -> sub.onError(t));
                }

                @Override
                public void onComplete() {
                    es.execute(() -> sub.onComplete());
                }
            });
        };

        // subscribe
        pubOnPub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {} ", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        log.debug("EXIT");
    }
}
```
### 결과
```
> Task :SchedulerEx.main()
[subOn-1] DEBUG SchedulerEx - onSubscribe
[main] DEBUG SchedulerEx - EXIT
[subOn-1] DEBUG SchedulerEx - request()
[pubOn-1] DEBUG SchedulerEx - onNext : 1 
[pubOn-1] DEBUG SchedulerEx - onNext : 2 
[pubOn-1] DEBUG SchedulerEx - onNext : 3 
[pubOn-1] DEBUG SchedulerEx - onNext : 4 
[pubOn-1] DEBUG SchedulerEx - onNext : 5 
[pubOn-1] DEBUG SchedulerEx - onComplete
```

## flux로 만들어보기
```
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxScEx {
    public static void main(String[] args) {
        Flux.range(1, 10) // 1-10까지 publish하는 publisher가 생김
                .publishOn(Schedulers.newSingle("pub"))
                .log()
                .subscribeOn(Schedulers.newSingle("sub")) // subscriber하는 시점부터 single thread를 만들어준다.
                .subscribe(System.out::println);
    }
}
```
### 결과
```
[main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
[sub-1] INFO reactor.Flux.PublishOn.1 - | onSubscribe([Fuseable] FluxPublishOn.PublishOnSubscriber)
[sub-1] INFO reactor.Flux.PublishOn.1 - | request(unbounded)
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(1)
1
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(2)
2
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(3)
3
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(4)
4
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(5)
5
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(6)
6
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(7)
7
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(8)
8
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(9)
9
[pub-2] INFO reactor.Flux.PublishOn.1 - | onNext(10)
10
[pub-2] INFO reactor.Flux.PublishOn.1 - | onComplete()
```

## 주의 할 점
- 백그라운드에서 진행되기 때문에 계속해서 main thread에서 살아 있을 수 있다.
- 아래와 같은 작업을 진행해줘야함.
```
@Override
public void onComplete() {
    log.debug("onComplete");
    es.shutdown();
}
```

- daemon thread
```
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {

        Flux.interval(Duration.ofMillis(500))
                .take(10)
                .subscribe(System.out::println); // 실행이 안됨.
        //[main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework

        TimeUnit.SECONDS.sleep(10);
    }
}
```
- userthread는 main thread가 종료되어도 살아있음.
- daemon thread : jvm 이 daemon 스레드만 남아있으면 종료함 ex ) timer thread
## Interval
- 데이터를 선별적으로 사용해서 시뮬레이션 돌릴 때 편리함.

## take
 - operator 자체를 control하는 것.
 - cancel 사용해보기
```
@Slf4j
public class IntervalEx {
    public static void main(String[] args) {
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                int no = 0;
                volatile boolean cancelled = false;

                @Override
                public void request(long n) {
                    ScheduledExecutorService exes = Executors.newSingleThreadScheduledExecutor();
                    exes.scheduleAtFixedRate(() -> {
                        if (cancelled) {
                            exes.shutdown();
                            return;
                        }
                        sub.onNext(no++);
                    }, 0, 300, TimeUnit.MILLISECONDS);
                }

                @Override
                public void cancel() {
                    cancelled = true;
                }
            });
        };

        Publisher<Integer> takePub = sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                int count = 0;
                Subscription subsc;

                @Override
                public void onSubscribe(Subscription s) {
                    subsc = s;
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    sub.onNext(integer);
                    if (++count >= 5) {
                        subsc.cancel();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    sub.onError(t);
                }

                @Override
                public void onComplete() {
                    sub.onComplete();
                }
            });
        };

        takePub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext : {} ", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });
    }
}
```
### 결과
```
> Task :IntervalEx.main()
[main] DEBUG IntervalEx - onSubscribe
[pool-1-thread-1] DEBUG IntervalEx - onNext : 0 
[pool-1-thread-1] DEBUG IntervalEx - onNext : 1 
[pool-1-thread-1] DEBUG IntervalEx - onNext : 2 
[pool-1-thread-1] DEBUG IntervalEx - onNext : 3 
[pool-1-thread-1] DEBUG IntervalEx - onNext : 4 
```
