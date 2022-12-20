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
#### Durable
### Observer Pattern

### Reactive Streams
