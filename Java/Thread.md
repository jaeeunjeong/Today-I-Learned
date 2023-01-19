- [x] callable
- [x] runnable
- [ ] threadpool
- [ ] executer
- [ ] Future
- callable과 runnable
  - 스레드를 표현할 수 있는 객체
  - 리턴값 유무에 따라 구분된다. -> callable은 리턴값이 있고, runnable은 리턴값이 없다.
  - runnable
  ```
  Runnable task = new Runnable(){
    @Override
      public void run(){
        // 스레드가 처리할 작업 내용
      }
    }
  ```
  - callable
  ```
  Callable<T> task = new Callable<T> {
    @Override
      public T call() throws Exception{
        // 스레드가 처리할 작업 내용
      return T;
    }
  }
  ```
