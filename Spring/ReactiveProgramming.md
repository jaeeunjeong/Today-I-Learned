# ReactiveProgramming
## Reactive Streams
### Duality
#### Iterable
for each문은 Iterable을 구현한 것이다. 
```
public static void main(String[] args) {
    Iterable<Integer> iter = () -> 
        new Iterator<Integer>() {
            int i = 0;
            final static int MAX = 10;
            public boolean hasNext() {
                return i < MAX;
            }

            public Integer next() {
                return ++i;
            }
        };
    for(Integer i : iter) {
        System.out.print(i +" ");
    }
}
```
결과
```
1 2 3 4 5 6 7 8 9 10
```
#### Observable (Durable)
```
static class IntObservable extends Observable implements Runnable { // publisher, 데이터를 보내는 쪽
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            setChanged(); // 새로운 변화를 만들고
            notifyObservers(i); // 그 변화를 알려줌
        }
    }
}

public static void main(String[] args) {
    Observer ob = new Observer() { // subscriber, 데이터를 받는 쪽
        @Override
        public void update(Observable o, Object arg) {
            System.out.println(Thread.currentThread().getName()+ " " + arg);
        }
    };

    IntObservable io = new IntObservable();
    io.addObserver(ob);

    //외부 스레드에서 수행되도록 처리
    ExecutorService es = Executors.newSingleThreadExecutor();
    es.execute(io);

    System.out.println(Thread.currentThread().getName() + " EXIT");
    es.shutdown();
}
```
### Observer Pattern
- 데이터를 가져온 다음에 한꺼번에 보내주는, multicast가 가능하다.
- 비동기적으로 작업을 진행하고 필요할 때 가져올 수 있다.
- Iterator와 Observer 둘은 같은 기능을 하는데 방식이 pull/push로 다르다.
### Reactive Streams
- Publish, Subscriber 의 동작 패턴
```
package test;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PubSub {
    public static void main(String[] args) {
        // Publisher : Observable에 해당함. : 잠재적으로 한계가 없는 연속된 요소들을 제공하는 것
        // Subscriber : Observer에 해당함. : 제공을 받을 수 있는 것

        // 프로토콜을 따라야함. onsubscrebe는 반드시 호출 onnext는 호출해도 되고 안해도 되고 완전 옵션, onerror,oncomplete는 종료 호출인데 둘 중에 하나는 꼭 호출해야한다.

        ExecutorService es = Executors.newSingleThreadExecutor();

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
        Publisher<Integer> p = new Publisher<Integer>() { // 데이터를 주는 쪽임 -> 누구한테 데이터를 줘야하는 지 알아야함 -> 구독방식으로 알수 있음 subscrip으로 함.
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = itr.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) { // 이벤트를 요청하는 것. n개만큼 받고싶다.
                        es.execute(() -> {
                            int i = 0;
//                        Feature<?> f = es.submit(() -> { // 진행상황을 파악하고 싶을 때 사용
                            try {
                                // 요청을 해달라는 것
                                while (i++ < n) {
                                    if (it.hasNext()) {
                                        subscriber.onNext(it.next());
                                    } else {
                                        subscriber.onComplete();
                                        break;
                                    }
                                }
                            } catch (RuntimeException e) {
                                subscriber.onError(e);
                            }
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() { // 아까 프로토콜이랑 같음

            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) { // 필수 : 데이터를 어떻게 받을지 적어야함...
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) { // 다음 것을 처리하는 곳
                System.out.println("onNext" + item);
                this.subscription.request(1);
            }

            // 에러와 완료를 처리 : 둘 중 하나만 만들기.
            @Override
            public void onError(Throwable t) {
                // 구독 이후에 발생하는 오류는 전부 여기서 처리함.
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        p.subscribe(s);
    }
}

```
*Publisher가 subscribe를 제공하고, Subscriber가 그것을 호출한다.*


### Operators
- map
- sum
```
// operator
// 종류
// 1. map : 데이터를 가공해서 새로운 데이터를 만드는 것
@Slf4j
public class PubSub {
    public static void main(String[] args) {
        Publisher<Integer> pub = getIterPub();
        pub.subscribe(logSub());

        //
        Publisher<Integer> mapPub = mapPub(pub, (Function<Integer, Integer>) s -> s * 10);
        Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
        mapPub.subscribe(logSub());

        Publisher<Integer> sumPub = sumPub(pub);
        sumPub.subscribe(logSub());

        Publisher<Integer> reducePub = reducePub(pub, 0, (BiFunction<Integer, Integer, Integer>) (a, b) -> a + b);
        reducePub.subscribe(logSub());
    }

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
                        sub.onNext(result);
                        sub.onComplete();
                    }
                });
            }
        };
    }

    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    int sum = 0;

                    @Override
                    public void onNext(Integer i) {
                        sum += i;
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(sum);
                        sub.onComplete();
                    }
                });
            }
        };
    }

    // operator
    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new DelegateSub(sub) {
                    @Override
                    public void onNext(Integer i) {
                        sub.onNext(f.apply(i));
                    }
                });
            }
        };
    }

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
#### generic 으로 변환하기
```
@Slf4j
public class PubSub {
    public static void main(String[] args) {
        Publisher<Integer> pub = getIterPub();
        pub.subscribe(logSub());

        //
        Publisher<String> mapPub = mapPub(pub, s -> ":" + s + ":");
        //Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
        mapPub.subscribe(logSub());

        //Publisher<Integer> sumPub = sumPub(pub);
        //sumPub.subscribe(logSub());

        //Publisher<Integer> reducePub = reducePub(pub, 0, (BiFunction<T, T, T>) (a, b) -> a + b);
        //reducePub.subscribe(logSub());
    }

    // operator
    private static <T, R> Publisher<T> mapPub(Publisher<T> pub, Function<T, T> f) {
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

    private static <T, R> Publisher<R> reducePub(Publisher<T> pub,R init, BiFunction<R, T, R> f) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                pub.subscribe(new DelegateSub<T, R>(sub) {
                    R result = init;
                    @Override
                    public void onNext(T i) {
                        result - bf.apply(result, i);
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
}
```
