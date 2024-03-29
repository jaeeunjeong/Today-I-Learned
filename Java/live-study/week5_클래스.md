# 5주차 과제: 클래스
> 자바의 Class에 대해 학습하세요.

## 학습 목록
- [클래스 정의하는 방법](#클래스_정의하는_방법)
- [객체 만드는 방법 (new 키워드 이해하기)](#객체_만드는_방법_(new_키워드_이해하기))
- [메소드 정의하는 방법](#메소드_정의하는_방법)
- [생성자 정의하는 방법](#생성자_정의하는_방법)
- [this 키워드 이해하기](#this_키워드_이해하기)

## 과제
### int 값을 가지고 있는 이진 트리를 나타내는 Node 라는 클래스를 정의하세요.
### int value, Node left, right를 가지고 있어야 합니다.
### BinrayTree라는 클래스를 정의하고 주어진 노드를 기준으로 출력하는 bfs(Node node)와 dfs(Node node) 메소드를 구현하세요.
### DFS는 왼쪽, 루트, 오른쪽 순으로 순회하세요.

## 클래스 정의하는 방법
클래스 : 객체가 어떤 식으로 사용될지 정의해 놓은 공간.  

```
class Date{

}
```


## 객체 만드는 방법 (new 키워드 이해하기)
객체 : 클래스를 사용하기 위한 메모리 공간을 갖고 있는 변수. 클래스에 정의된 내용으로 만든 메모리.  
인스턴스 : 클래스를 사용하기 위해 객체화 한 것. 클래스로 부터 만들어진 객체.
객체는 선언하면 메모리 공간이 생기고, **new** 키워드를 이용하여 인스턴스를 만든다.  
인스턴스를 선언한 객체의 주소에 넣어주면 객체의 사용이 가능하다.  
```

String str; //객체의 선언
str = "HELLO!"; //객체의 초기화

class Dot{
   int x;
   int y
} 
Dot dot = new Dot(); //dot라는 메모리 공간을 만들고 new를 이용하여 Dot객체를 생성한 후 dot주소에 인스턴스를 대입한다.
```

## 메소드 정의하는 방법
메소드 : 기능이 구현되어 동작하는 부분  

```
public void discountDays(){
//1)     2)      3)
}
```

1) 접근 제어자  
- 사용 되는 이유 : 자바의 캡슐화! 외부에서 해당 메서드의 접근을 못하게 하기 위함  
2) 종류
- public : 외부 패키지에서도 접근 가능하다.  
- protected : 같은 패키지 내에서나 상속의 관계에 있다면 접근 가능하다.  
- private : 외부에서 직접 접근은 불가능하고 같은 클래스 내에서만 접근 가능하다.  
- default : 접근제어자를 선언하지 않으면 default 값인데, 동일한 패키지 내에서만 접근 가능하다.

2) 반환 값  
메서드가 return 하는 데이터타입을 뜻한다.  
void는 리턴하지 않음을 뜻하며, 그 외는 return 키워드를 이용하여 반드시 리턴을 해줘야한다.  

3) 메서드 이름  
메서드의 이름을 정의한다.  
CamelCase를 이용하여 이해하기 쉬운 메서드 이름을 정한다.  

## 생성자 정의하는 방법
생성자 : 인스턴스를 초기화하기 위한 메서드  
생성자가 있어야 인스턴스의 초기화가 가능하다.  
따라서 default 생성자라고 따로 생성자가 존재하지 않는다면 클래스와 동일한 이름의 매개 변수가 없는 생성자가 존재한다.  
```
class Date{
   int year;
   int month;
   int day;
   
   //default 생성자
   Date();
}
```
- 오버로딩하여 다양한 인자를 받는 생성자를 구현할 수 있다.
## this 키워드 이해하기
```
class Date{
   int year;
   int month;
   int day;
   
   Date(int year, int month, int day){
   this.year = year;
   this.month = month;
   this.day = day;
   }
}
```
this란 생성자 내부에서 생성자와 인스턴스를 구분하기 위해 사용한다.
this를 사용하면 자기 자신을 hashcode를 이용해 갖게된다.
이름이 동일할 경우 구분을 명확하게 하기 위해 사용한다.


   ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
