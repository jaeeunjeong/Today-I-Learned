# Reactive Streams
- Duality : 쌍대성
- Observer Pattern
- Reactive Streams

1. Iterable
- for each는 구현한 것이 아니라 Iterable를 이용해서 만든 것
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
2. Observavle ( Iterable의 쌍대성 관계 )
- Observavle : source
- Observer : 이 곳에 Event나 Data를 던져서 실행하는 곳
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
