# Annotation

## @RequestParam vs @PathVariable
- 둘 다 파라미터를 전달받기 위한 어노테이션
- @RequestParam의 경우 쿼리, form의 data, multipart의 data 등의 값을 전달받고,  
- @PathVariable RESTful URL 주소 형태의 parameter 값을 전달받는다. 
- 공통적으로 required() 속성을 통해 파라미터의 값이 필수인지 아닌지 정할 수 있으며, default는 true이다.  
 false로 정하면 파라미터가 null이어도 오류가 발생하지 않는다. 
### @PathVariable
```
  @GetMapping("communuty/read/{postId}")
  public Posts readPost(@PathVariable int postId){
      return postsService.readPost(postId);
  }
```
- URI 템플릿 변수와 메서드 파라미터가 맵핑되어 사용한다.  
- @RequestMapping 계열의 annotation에서 사용한다.(@PostMapping/@GetMapping)  

### @RequestParam 
```
  @GetMapping("communuty/home")
  public List<Posts> selectPostsList(@RequestParam (required = false) int page){
      return postsService.selectPostsList();
  }
```
- web에서 요청하는 파라미터를 맵핑하여 사용된다.
- post 형태로 보내는 파라미터, form 형태의 파라미터, Multipart 형태의 파라미터가 맵핑된다.

### @Data
- @Getter : 생성된 객체를 getter로 만들어준다.
- @Setter : public 으로 생성된 객체를 setter로 만들어준다.
- @RequiredArgsConstruct : private final로 선언된 객체를 포함하는 생성자를 만들어준다.
- @ToString : 디버깅이나 로그를 남기기 위해 사용한다.
- @EqualsAndHashCode : equals와 hashcode를 만들어서 객체를 비교를 해준다.
  - Equals : 객체의 값이 같은지 확인 (동등성)
  - Hashcode : 객체의 고유 값이 같은지 확인 (동일성)
- @Value : 프로퍼티를 읽을 때 사용

### @Transaction
 - 스프링 프레임워크에서 제공하는 선언적 트랜잭션을 처리할 수 있는 어노테이션
 - 클래스/ 메서드에 붙을 수 있으며, 메서드에 선언된 값들이 모두 수행되어야 데이터베이스에 커밋된다.
 - 옵션
    - propagation
        - 트랜잭션 동작 중에 다른 트랜잭션이 수행될 때, 각 트랜잭션 간의 간섭에 대해 어떻게 다룰지를 ㅇ지정하는 옵션
    - isolation
        - 트랜잭션의 격리 수준에 대해 결정한다.
    - noRollbackFor=*Exception.class*
        - 특정 예외 발생시 rollback 하지 않는다.
    - rollbackFor=*Exception.class*
        - 특정 예외 발생시 rollback 한다.
    - timeout
        - 지정된 시간안에 완료되지 않으면 rollback한다.
    - readOnly
        - 트랜잭션을 읽기전용으로 설정한다.
        - 특정 트랜잭션안에서 쓰기 작업이 일어나는 것을 방지하기 위해 사용. 

### @Valid vs @Validated

### @Valid

- DTO 에 제약조건을 명시하고 컨트롤러 파라미터 앞에 선언하면 빈 검증기를 이용해서 제약조건을 검증하도록 지시하는 어노테이션.
- ArgumentResolver에서 처리한다. 따라서 Controller에서만 동작한다.
- JSR Java 표준 스펙에서 검증해준다.
- 관련 예외 : MethodArgumentNotValidException
- 의존성 추가해줘야한다.

```java
implementation group: 'org.springframework.boot', name: 'spring-boot-starter-validation'
```

### @Validated

- Spring Framework에서 AOP 기반으로 제공하는 기능
- 클래스에 @Validated를 붙이고 메서드에 @Valid를 붙여서 유효성을 검증한다.
- 관련 예외 : ConstraintViolationException

### 참고
spring document
