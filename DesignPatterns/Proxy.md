# Proxy 
- 정의
  1. 어떠한 객체를 사용하고자 할 때,  직접 사용하는 것이 아니라, **대항하는 객체를 통해 대상 객체에 접근하는 방식**
- 특징
  1. 실제 객체의 접근 제어자를 숨기고 인터페이스를 통해 노출시킬 수 있다.
  2. 인터페이스로 선언하고 실행할 구체 클래스를 초기화해서 사용하는 방식. -> 직접 접근하지 않음 **(흐름 제어)**
- 예시
```
public interface ServiceInterface {
    String run();
}
```
```
public class Service implements ServiceInterface {
    @Override
    public String run() {
        return "CALL class service";
    }
}
```
```
public class Proxy implements ServiceInterface {

    ServiceInterface serviceInterface;

    @Override
    public String run() {
        serviceInterface = new Service();
        return serviceInterface.run();
    }
}
```
```
public class Main {
    public static void main(String[] args) {
        ServiceInterface serviceInterface = new Proxy();
        System.out.println(serviceInterface.run());
    }
}
```
결과  
```
CALL class service
```
인터페이스로 선언한 것 **(interface ServiceInterface)** 을 구현한 클래스 **(class Service)** 을  
프록시 객체 **(class Proxy)** 에 담아서 호출하는 흐름임!!!
