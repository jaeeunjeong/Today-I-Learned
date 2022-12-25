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
<-> durable
#### Durable/ Observable
```
static class IntObservable extends Observable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            setChanged(); // 새로운 변화를 만들고
            notifyObservers(i); // 그 변화를 알려줌
        }
    }
}

public static void main(String[] args) {
    Observer ob = new Observer() {
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

        // 프로토콜을 따라야함. onsubscrebe는 반드시 호출 onnext는 호출해도 되고 안해도 되고, onerror,oncomplete는 완전 옵션

        ExecutorService es = Executors.newSingleThreadExecutor();

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
        Publisher<Integer> p = new Publisher<Integer>() { // 데이터를 주는 쪽임 -> 누구한테 데이터를 줘야하는 지 알아야함 -> 구독방식으로 알수 있음 subscrip으로 함.
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = itr.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
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
