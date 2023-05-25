# Entity, VO, DTO
> 데이터를 다루기 위한 객체
## Entity
* DB와 밀접하며,객체와 1대1로 직접 맵핑되는 객체
* 도메인 로직만을 가진다.
* 일관성 유지를 위해 Setter의 사용을 지양해고 생성자를 이용한 주입을 권장한다.
* JPA에서 사용하려면 기본생성자는 필수인데, 접근제어자를 protected로 한다.

### 관련 Anotation
- @Index
  - 인덱스를 지정해줄 수 있다.
  - 관련 속성  
    - name : 인덱스의 이름
    - columnList : 지정할 인덱스
    - unique : unique 속성 true/false로 지정
- TYPE 단위(CLASS)
  - @Entity
    - 테이블과 맵핑되기 위해 사용하는 어노테이션  
    - JPA가 관리하는 클래스가 된다.  
    - JPA가 관리하는 클래스가 되면 기본 생성자가 필요하다!  
  - @NoArgsConstructor(access = AccessLevel.PRIVATE)
    - JPA는 영속성을 위해 기본 생성자가 필요한데, 엔티티부분은 기본 생성자가 무분별하게 생성되면 데이터의 일관성이나 안정성이 떨어진다.
-  METHOD, FIELD 단위
  - @Id
    - PK를 지정하는 어노테이션  
  - @GeneratedValue
    - PK는 유일한 속성을 가져야하는데,  GeneratedValue의 (strategy = GenerationType.IDENTITY) 속성등을 통해 유일성을 보장해줄수 있다.

## VO
* 비즈니스 로직을 가지는 객체
* getter/setter를 가져야하며 필드값이 같다면 서로 다른 객체를 같은 객체로 본다.
* equals와 hashcode를 오버라이드해서 사용해야한다.
## DTO
* 계층간의 데이터 교환을 위한 객체
* 데이터를 가공하지 않고 오직 전달만을 위한 객체이다.
