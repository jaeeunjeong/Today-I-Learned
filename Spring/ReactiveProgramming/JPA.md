# Spring에서 JPA 사용시 주의 사항
Reactor에서 map()에서 다른 메서드를 호출하면 map()과 다른 쓰레드에서 실행된다. -> 트랜잭션이 종료된 환경에서 실행된다.  
따라서 LazyInitializationException이 발생할 수 있다.  
## 해결책
- entity를 트랜잭션이 종료되기 전에 미리 loading하기
- Mono.map에 새로운 트랜잭션을 열고 새로운 entity로 작업하기
