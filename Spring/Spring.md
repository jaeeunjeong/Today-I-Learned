# Dependency
다른 관계에 있는 객체들의 사용  
두 객체 간의 연결  
어떠한 기능을 사용하기 위해 다른 객체의 도움이 필요하여 그 객체를 사용할 때 의존한다고 한다.    

> 예를 들어,  
 Bread라는 클래스가 있고 Bread라는 클래스는 Oven이라는 클래스를 필요로 한다.  
**이때, Bread클래스는 Oven클래스에 대한 의존성을 가진다고 한다.**  
Pizza 클래스도 Oven클래스를 필요로 한다.  
그래서 Pizza 전용 Oven 클래스에 대한 의존성을 가지고 Oven 클래스를 만든다. **(의존성)**  
근데, Bread, Pizza 클래스 둘다 같은 성격의 Oven 클래스를 사용하고 있고,   
Oven의 성격이 변해서 Bread, Pizza와 관련된 Oven들을 모두 변경해줘야한다면,  
번거롭게 굳이 둘을 따로 만들어서 쓸 필요가 없다.  
공통된 하나의 Oven만 사용하고 우린 Oven 클래스를 호출만으로 사용하자.**(DI)**  
하나의 Oven 사용을 통해 **관리의 용이**(청소와 같은 것을 한 번만 해도 됨!),   
**유연성**(Bread, Pizza 등등 한 오븐에서 여러 음식을 만들 수 있음!) 등의 장점 증가!  

타입에 의존한다는 것은 해당 타입의 객체를 사용한다는 것.  
## IoC
- 스프링 객체(빈)을 프레임 워크가 관리하는 것을 의미
- 사용자가 개발에만 집중할 수 있다.
## DI
- IoC를 구현한 것을 의미

## 객체를 주입 받는 방식  
1. 필드 주입
```java
@Service
public class StoreServiceImpl implements StoreService {
	@Autowired
	private StoreRepository storeRepository;
}
```
- 간편한 코딩이 장점
- 불편성을 보장할 수 없다.
- 순환 참조가 발생할 수 있다.
2. Setter를 이용한 주입

```java
@Service
public class StoreServiceImpl implements StoreService {

	private StoreRepository storeRepository;

	@Autowired
	public void setStoreRepository(StoreRepository storeRepository){
	this.storeRepository = storeRepository;
}
```
- 의존 관계를 쉽게 파악하기 어렵다.
- 순환 참조가 발생할 수 있다.
3. 생성자 주입

```java
@Service
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;
	
	@Autowired
	public StoreServiceImpl(StoreRepository storeRepository){
		this.memberRepository = memberRepository;	
	}
}
```

- 생성과 동시에 의존성을 주입하여 안정적이다.
- 순환 참조를 컴파일 단계에서 확인 가능하다.
# POJO
  Plain Old Java Object  
  extends, implements, annontaion을 사용하지 않고 개발하는 것.  
  구속되지 않은 java object  
  
# 
