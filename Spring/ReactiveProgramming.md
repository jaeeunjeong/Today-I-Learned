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
    Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println(arg);
        }
    };
    IntObservable io = new IntObservable();
    io.addObserver(observer);

    io.run();
}
```
### Observer Pattern

### Reactive Streams
