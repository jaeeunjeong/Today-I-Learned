## 정의
    - T 타입 객체를 포장해주는 wrapper class
    - wrapper class이기 때문에 비용이 높은 편임.
## 사용법
    - of 나 ofNullable을 이용하는데, of는 null이 들어갈 수 있고, ofNullable은 null이라면 지정된 값이 대신 들어간다.
    
    ```java
    Optional<String> opt = Optional.ofNullable("apple");
    System.out.println(opt.get());
    ```
    
    ```java
    apple
    ```
    
## 기능
    1. isPresent() : null인지 확인하는 메서드
    2. orElse() : 저장된 값이 존재하면 저장된 값 리턴, 저장된 값이 없다면 else에 전달된 인수 값 리턴
    3. orElseGet() : 저장된 값이 존재하면 저장된 값을 리턴하고, 아니라면 람다 표현식 결과를 리턴한다.
    
### 참고

http://www.tcpschool.com/java/java_stream_optional
