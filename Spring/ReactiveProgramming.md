# ReactiveProgramming_
## Schedulers
```
@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {
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

        // publishOn : 하나일 경우
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.execute(() -> pub.subscribe(sub));
        };

        // 여러개일 경우 publishOn 동작 방식
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor();

            pub.subscribe(new Subscriber<Integer>() {
                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
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
        System.out.println("EXIT");
    }
}
```
```
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                public String getThreadNamePrefix() {
                    return "subOn-";
                }
            });
            es.execute(() -> pub.subscribe(sub));
        };

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

                }

                @Override
                public void onNext(Integer integer) {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
            });
        };
```
### flux로 만들어보기
```
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class FluxScEx {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("pub"))
                .log()
                .subscribeOn(Schedulers.newSingle("sub"))
                .subscribe(System.out::println);
    }
}
```
결과
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
```
public class FluxScEx {
    public static void main(String[] args) throws InterruptedException {
//        Flux.range(1, 10)
//                .publishOn(Schedulers.newSingle("pub"))
//                .log()
//                .subscribeOn(Schedulers.newSingle("sub"))
//                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(500))
                .take(10)
                .subscribe(System.out::println); // 실행이 안됨.
        //[main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework

        TimeUnit.SECONDS.sleep(10);
    }
}
```
```
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx {
    public static void main(String[] args) {
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                int no = 0;
                boolean cancelled = false;

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

        pub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Integer s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
```
