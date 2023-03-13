# 
- [x] callable
- [x] runnable
- [ ] threadpool
- [ ] executer
- [ ] Future
## Thread
- 프로세스의 실행 단위
## Threadpool
- Thread는 생성 및 종료에 자원이 많이 쓰여서 대기 상태로 둠으로써 효율적으로 자원을 관리하는데 이때 Thread들이 모여있는 공간을 Threadpool이라고 한다.
## Callable 인터페이스 와 Runnable 인터페이스
  - 스레드를 표현할 수 있는 객체
  - 리턴값 유무에 따라 구분된다. -> Callable은 리턴값이 있고, Runnable은 리턴값이 없다.
  - Runnable
  ```
  Runnable task = new Runnable(){
    @Override
      public void run(){
        // 스레드가 처리할 작업 내용
      }
    }
  ```
  - Callable
  ```
  Callable<T> task = new Callable<T> {
    @Override
      public T call() throws Exception{
        // 스레드가 처리할 작업 내용
      return T;
    }
  }
  ```
