# java.util.concurrent
## Fork/Join

- 작업을 여러 개로 효율적으로 병렬처리하기 위해 등장한 기능
- fork : 작업 단위를 나누는 것
- join : 작업 단위를 합친다.

### 관련 클래스

- ForkJoinPool : 등록된 Task를 관리한다.
- ForkJoinTask : ?
- RecursiveTask : 실제로 업무적으로 수행할 일을 구현할 클래스
- RecursiveAction : 실제로 수행할 일이 있지만 값을 리턴하지 않는다.

## Code

```java
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
				// 메인 스레드에서 실행하는 로직
        List<Integer> numbers = new Random().ints(1000, 1, 100)
                .boxed()
                .toList();

        int sum = numbers.stream().mapToInt(Integer::intValue).sum();

        log.info("SINGLE THREAD :: {}", sum);

				// 병렬처리

				// 1. pool을 만들어서 Task를 관리함.
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

				// 2. 병렬처리하기 위해 Task를 만듦
        SumTask sumTask = new SumTask(numbers);

				// 3. 병렬로 처리한 값을 가져옴.
        ForkJoinTask<Integer> submit = forkJoinPool.submit(sumTask);
        Integer result = submit.get();

        log.info("MULTI THREAD :: {}", result);
    }
}

@Slf4j
class SumTask extends RecursiveTask<Integer> {

    private final List<Integer> numbers;

    public SumTask(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    protected Integer compute() {
        if (numbers.size() > 10) {
            return numbers.stream().mapToInt(Integer::intValue).sum();
        }

        List<SumTask> subTasks = partitionTask(this.numbers);

        return ForkJoinTask.invokeAll(subTasks)
                .stream()
                .mapToInt(ForkJoinTask::join)
                .sum();
    }

    private List<SumTask> partitionTask(List<Integer> numbers) {
        List<Integer> part1 = numbers.subList(0, numbers.size() / 2);
        List<Integer> part2 = numbers.subList(numbers.size() / 2, numbers.size());

        SumTask sumTask1 = new SumTask(part1);
        SumTask sumTask2 = new SumTask(part2);

        List<SumTask> result = new ArrayList<>();
        result.add(sumTask1);
        result.add(sumTask2);

        return result;
    }
}
```

## result

```java
20:34:13.284 [main] INFO Main - SINGLE THREAD :: 50159
20:34:13.287 [main] INFO Main - MULTI THREAD :: 50159
```