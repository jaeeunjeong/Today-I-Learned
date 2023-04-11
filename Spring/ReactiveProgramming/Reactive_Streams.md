# Reactive Streams
## 목차
- Iterable
- Observable
## Iterable
   - for each는 Collection을 구현한 것이 아니라 Iterable를 이용해서 만든 것
   - pulling 방식 : 소스가 있다면 값을 받는 방식
### 코드
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
### 결과
```
1 2 3 4 5 6 7 8 9 10
```
## Observable ( Iterable의 쌍대성 관계 )
*쌍대성 : 구조가 정 반대인 성질 (대칭성과 비슷한 의미)*
  - Observable : source, Event나 Data 같은 데이터의 근원
  - Observer : 이 곳에 Event나 Data를 던져서 실행하는 곳
  - push 방식 : 데이터나 결과를 주는 방식
  - 관심이 있는 Observer에 Broadcast 가능함.
### 코드
```
/**
* Observable : Observer를 등록하면 관련 event가 발생후 전달(Noti)해줌.
* Runnable : 비동기적으로 실행시키기 위해 사용
*/
static class IntObservable extends Observable implements Runnable { // publisher, 데이터를 보내는 쪽
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            setChanged(); // 새로운 변화를 만들고
            // 1)
            notifyObservers(i); // 그 변화를 알려줌 // push , ( = int i = it.next() // pull)
        }
    }
}

public static void main(String[] args) {

    // 2)
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
- 2) 에서 연산이 수행되지만, 실질적 데이터가 있는 곳은 1)임.
### 결과
```
> Task :Ob.main()
main EXIT
pool-1-thread-1 1
pool-1-thread-1 2
pool-1-thread-1 3
pool-1-thread-1 4
pool-1-thread-1 5
pool-1-thread-1 6
pool-1-thread-1 7
pool-1-thread-1 8
pool-1-thread-1 9
pool-1-thread-1 10
```
- 외부 스레드에서 수행되도록 했기 때문에 **main EXIT**가 나중에 작성되었지만 먼저 출력된다.

## Observer pattern 의 단점
1. Complete 개념이 없음
   - 종료 개념의 부재
2. Error 개념의 부재
   - exception이 발생해도 후에 처리할 방법이 없음.

## Reacitve Streams
- 리액티브 스트림의 표준
### API
1. Processor
2. Publisher
  - **Observable**에 해당함
  - 잠재적으로 한계가 없는 연속된 요소들을 Subscriber에 제공하는 것
  - `Publisher.subscribe(Subscriber)`를 호출해서 제공한다.
  - 아래의 프로토콜을 따라야한다.
    - `onSubscribe onNext* (onError | onComplete)`
      - onSubscribe : 반드시 호출, Subscriber안에 위치함
      - onNext : 호출해도 되고 안해도 되고 완전 옵션, 
        - 다음 것을 처리하는 곳, observer pattern에서 update와 동일
      - onError, onComplete : 종료 호출인데 둘 중에 하나는 꼭 호출해야하며 종료 시그널임.
         - onError : try-catch의 역할을 수행. 
    - 데이터를 제공하는 곳이며 구독의 형태로 데이터를 줘야하는 지 알려줘야한다.
3. Subscriber : **Observer**에 해당하며 데이터를 제공받는다.
4. Subscription : 중개의 역할, 백프레셔, pub와 sub간의 속도를 조절하는 역할.
### Reacitve Streams 구현해보기
### 코드
```
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PubSub {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newSingleThreadExecutor();
        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5); // DB에서 가져온다면 Connection Data일 것임.

        // 1. 실제 수행되길 원하는 로직
        Publisher<Integer> p = new Publisher<Integer>() {
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

        // 2.
        Subscriber<Integer> s = new Subscriber<Integer>() { // 아까 프로토콜이랑 같음

            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) { // 필수 : 데이터를 어떻게 받을지 적어야함...
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1); // 요청한 데이터를 몇개 받고 싶다고 전달하는 것.
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext" + item);
                this.subscription.request(1); // 요청 끝나면 다음에 또 작업을 요청하기 위함.
            }

            // 에러와 완료를 처리 : 둘 중 하나만 만들기.
            @Override
            public void onError(Throwable t) {
                // 구독 이후에 발생하는 오류는 전부 여기서 처리함.
                // try catch를 여기서 처리.
                System.out.println("onError" + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        // 3. 데이터를 구독
        p.subscribe(s);

        es.awaitTermination(1, TimeUnit.HOURS);
        es.shutdown();
    }
}
```
### 결과
```
> Task :PubSub.main()
main: onSubscribe
pool-1-thread-1 onNext1
pool-1-thread-1 onNext2
pool-1-thread-1 onNext3
pool-1-thread-1 onNext4
pool-1-thread-1 onNext5
pool-1-thread-1 onComplete
```
### 참고
https://www.youtube.com/watch?v=8fenTR3KOJo
