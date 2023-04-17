# 개념 정리

배치 기본 테이블

- 파라미터 관리를 위한 테이블이 있음.
- 배치에 사용되는 파라미터들을 관리하는데 왜 관리를 하는 거지
- → 데이터 잘못들어가서 다시 돌리면 저게 되는 건가… 이해가 안 됨쓰.

Meta Table

- BATCH_JOB_INSTANCE
    - Job이 실행 될 때, 파라미터 관련된 테이블
    - 배치가 실행 될 때, 사용된 배치 파라미터가 들어감
    - 동일한 파라미터가 들어오면 실패처리되어 예외가 발생함
    - 배치 중에 실패하면 생성되지 않음
- BATCH_JOB_EXECUTION
    - BATCH_JOB_INSTANCE의 실행 내역을 갖고있으며, BATCH_JOB_INSTANCE의 부모임.

# 기본 Batch Flow

```java

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private final JobRepository jobRepository;

    @Bean
    public Job simpleJob() {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleTest())
                    .on("FAILED") // if문처럼 ExitStatus를 캐치하고 그 결과에 따라 to로 이동한다.
                    .to(simpleStepFail())
                    .on("*")
                    .end() // FlowBuilder를 반환하는 end, 계속해서 from을 이어갈 수 있음.
                .from(simpleTest()) // 일종의 이벤트 리스너, 이 배치의 상태를 캐치하여 on에서
                    .on("*")
                    .to(simpleStepEtcCase())
                    .next(simpleTest())
                    .on("*")
                    .end()
                .from(decider())
                    .on("Odd")
                    .to(simpleStepEtcCase())
                .end() // FlowBuilder를 종료하는 end
                .build();
    }

    private Step simpleStepEtcCase() {
        return new StepBuilder("simpleStepEtcCase", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>> This is simpleStepEtcCase");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    private Step simpleStepFail() {
        return new StepBuilder("simpleStepFail", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>> This is simpleStepFail");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step simpleTest() {
        return new StepBuilder("simpleTest", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>> This is simpleTest");
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })).build();
    }
```

## Job

- 배치 프로세스의 단위

## Start

- 배치의 시작

## From

- 일종의 이벤트리스너
- 배치의 상태를 캐치해서 On에서 사용가능

## End

- Flow를 종료하거나 반환할 때 사용

## Step

- 배치의 Job의 단위
- 수행할 작업들이 명시되어 있다.
- 인스턴스들을 포함하는 컨테이너

## Next

- Step들을 순차적으로 연결하기 위해 사용

## On

- 배치를 수행하다가 예외처리가 필요할 때 사용
- ExitStatus의 상태를 캐치하여 사용한다.
- ExitStatus외의 상태가 필요하다면 StepExecutionListener를 implements 하여 사용한다.

```java
// ExitStatus에 값이 없어서 예외처리를 해야하는 경우
class SkipCheckingListener implements StepExecutionListener{
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return new ExitStatus("TELL");
    }
  }
```

## Decider

- 분기처리를 위해 사용된다.
- JobExecutionDecider를 implements하여 사용.

```java
@Bean
    public JobExecutionDecider decider() {
        return new OddDecider();
    }
    public class OddDecider implements JobExecutionDecider {
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            return new FlowExecutionStatus("Odd");
        }
    }
```

## Job Parameter

- 외부/내부에서 파라미터를 전달받아 Batch에서 사용가능한데 이 때의 파라미터를 의미
- @Value를 이용해서 사용한다.
- 사용하기 위해서 Batch 전용 Scope 를 선언해야한다.
    - @StepScope
        - Tasklet/ ItemReader, ItemWriter, ItemProcessor 에서 사용
    - @JobScope
        - Step 선언문에서 사용
    - 각각의 선언된 시점에서 실행되어 Bean이 생성된다.
        - Bean의 생성 시점을 지정된 Scope 시점으로 지연 가능함. **Late Binding**
        - 동일 컴포넌트를 병렬/ 동시에 사용할 때 유리 → 서로 다른 step에서 하나의 tasklet을 사용하려고 할 때, 동시에 접근이 가능하면 서로에게 침범할 수 있지만, (데이터가 꼬일 수 있다.) 별도로 Scope를 선언하기 때문에 침범할 일이 없다.

- Batch 전용 Scope 을 생성할 때만 Job Parameters가 생성된다.

```java
@Bean
@Scope

@Scope(~~~~~)
```

## Chunk

- 데이터 덩어리로, 작업이 수행되는 단위들의 수
- **chunk 단위로 트랜잭션을 다룬다.**
- 실패할 경우 해당 chunk만큼 rollback 된다.
- Reader와 Processor은 1건씩 다뤄지고,  Writer은 chunk 단위로 처리된다.
- Reader → Processor → Writer
    - Reader : 데이터를 하나씩 읽어온다.
    - Processor : 읽어온 데이터를 가공한다.
    - Writer : 가공된 데이터를 모은 뒤, chunk 단위만큼 쌓였다면 writer에 전달하고 일괄 저장한다.

## ****ChunkOrientedTasklet****

- chunk 지향 처리를 위한 클래스

## SimpleChunkProcessor

- processor와 writer로직 수행
- chunkSize만큼 쌓인 데이터들을 doProcessor로 전달하고 가공된 데이터를 writer을 통해 일괄 저장된다.

## Page Size

- 한 번에 조회할 Item의 양
- 일정 기준 값을 넘어가면 page의 갯수(한번에 조회할 Item의 양) 대로 조회한다. // 1)
- PagingItemReader

```java
@Nullable
@Override
protected T doRead() throws Exception {

	synchronized (lock) {

		if (results == null || current >= pageSize) { // 1)

			if (logger.isDebugEnabled()) {
				logger.debug("Reading page " + getPage());
			}

			doReadPage();
			page++;
			if (current >= pageSize) {
				current = 0;
			}

		}

		int next = current++;
		if (next < results.size()) {
			return results.get(next);
		}
		else {
			return null;
		}

	}

}
```
