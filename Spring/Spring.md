# Dependency
다른 관계에 있는 객체들의 사용  
두 객체 간의 연결  
어떠한 기능을 사용하기 위해 다른 객체의 도움이 필요하여 그 객체를 사용할 때 의존한다고 한다.  
*물건을 자르기 위해 가위가 필요할 때 가위를 사용하는 것을 의존한다고 하는 것 같은(?)*  
타입에 의존한다는 것은 해당 타입의 객체를 사용한다는 것.  
객체를 주입 받는 방식  
1. 의존하는 타입의 객체를 직접 생성
```
  StringTokenizer st;
  st = new StringTokenizer(br.readLine());
  int T = Integer.parseInt(st.nextToken());
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

# DI
  Dependency에 대한 설계 패턴
  Construct Injection이 요즘 대세

# POJO
  Plain Old Java Object  
  extends, implements, annontaion을 사용하지 않고 개발하는 것.  
  구속되지 않은 java object  
  
# 
