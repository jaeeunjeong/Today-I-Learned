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
1. 의존하는 타입의 객체를 직접 생성

```
  StringTokenizer st;
  st = new StringTokenizer(br.readLine());
  int T = Integer.parseInt(st.nextToken());
```
또는 @Autowired를 이용한다. 
```
  public class Calculater{
    @Autowired
    private Sum sum;
  }
```
2. 생성자를 이용하여 객체를 생성
```
  public class Calculater{
    private Sum sum;
  
    public Calculater(Sum sum){
      this.sum = sum;
    }
  
  }
```
생성자의 파라미터를 이용하여 의존하는 타입의 객체를 전달받을 수 있다.  
생성자를 이용해 의존 객체를 전달 받는 경우, 객체를 생성할 때 의존하는 객체를 생성자의 파라미터로 전달해야한다.  
객체를 생성하는 시점에서 의존하는 객체를 모두 전달받을 수 있다.  
전달 받은 파라미터가 정상인지 확인하는 코드를 생성자에 추가할 경우, 객체 생성 이후에는 그 객체가 사용가능한 상태임을 보장할 수 있다.  
하지만, 생성자에 전달되는 파라미터만으로는 실제 타입을 알아내기 어렵고 생성자에 전달되는 파라미터 갯수가 늘어날 수록 가독성이 떨어진다.  
순환참조를 방지하기때문에 스프링에서 권장하는 방식이기도 하다.  
final 선언이 가능하기때문에 immutable하게 사용할 수 있다.  
테스트 코드 작성시에도 편리하게 사용 가능하다.  

3. 프로퍼티를 이용하여 객체를 생성
의존 객체를 전달 받기 위해 메서드를 이용한다.
```
  public class Calculater{
    private Sum sum;
  
    public void setSum(Sum sum){
      this.sum = sum;
    }
  
  }
```
어떤 의존 객체를 설정하는지 메서드 이름으로 알 수 있다.  
객체를 생성한 뒤에 의존 객체가 모두 생성되었다고 장담할 수 없어서 사용이 불가능한 상태일 수도 있다.

# POJO
  Plain Old Java Object  
  extends, implements, annontaion을 사용하지 않고 개발하는 것.  
  구속되지 않은 java object  
  
# 
