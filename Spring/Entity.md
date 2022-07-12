# Entity
데이터베이스의 객체와 1대1로 맵핑되는 클래스

# Anotation
## @Index
인덱스를 지정해줄 수 있다.
관련 속성  
- name : 인덱스의 이름
- columnList : 지정할 인덱스
- unique : unique 속성 true/false로 지정
## TYPE 단위(CLASS)
### @Entity
- 테이블과 맵핑되기 위해 사용하는 어노테이션  
- JPA가 관리하는 클래스가 된다.  
- JPA가 관리하는 클래스가 되면 기본 생성자가 필요하다!  

### @Getter
테이블 객체의 값을 가져오기 위해 선언

### @NoArgsConstructor(access = AccessLevel.PRIVATE)
JPA는 영속성을 위해 기본 생성자가 필요한데, 엔티티부분은 기본 생성자가 무분별하게 생성되면 데이터의 일관성이나 안정성이 떨어진다.

## METHOD, FIELD 단위
## @Id
PK를 지정하는 어노테이션  
## @GeneratedValue
PK는 유일한 속성을 가져야하는데,  GeneratedValue의 (strategy = GenerationType.IDENTITY) 속성등을 통해 유일성을 보장해줄수 있다.
