# 11주차 과제: Enum 
> 자바의 열거형에 대해 학습하세요. 
## 학습 목록 
- [enum 정의하는 방법](#enum_정의하는_방법)
- [enum이 제공하는 메소드 (values()와 valueOf())](#enum이_제공하는_메소드_(values()와_valueOf()))
- [java.lang.Enum](#java.lang.Enum)
- [EnumSet](#EnumSet)

## enum 정의하는 방법
### enum이란
관련있는 **상수**들을 묶어서 그룹처럼 사용하는 자료형을 말한다.
Java에는 1.5부터 등장하였다.
### java의 enum의 특징
 - 상수는 값이 바뀌면 새로 컴파일 해야하지만 enum 은 다시 컴파일 하지 않아도 된다.
 - java에서는 타입까지 검증하기 때문에 **typesafe enum** 이라고 할 수 있다.
 - final, static의 속성을 갖는다.
### enum의 정의와 값 넣기
enum도 기본 생성자가 있지만 enum의 값 추가 속성을 부여하고 싶다면, 변수를 선언하고 생성자를 이용하여 값을 넣어줄 수 있다.  
참조 할 떄는, static 변수를 참조하는 것과 동일히다.  
> 코드
```
enum Port {
  http(80), https(443), ftp(20), ssh(22), etc1, etc2;
  
  private int portNumber;
  
  private Port() {}

  private Port(int portNumber) {
    this.portNumber = portNumber;
  }
}
enum Fruit{
  etc1, etc2;
}
```
```
public static void main(String args[]) throws Exception {
  // enum 확인.
  System.out.println("enum 값 확인");
  for (Port o : Port.values())
    System.out.println(o +" " + o.portNumber);

  System.out.print("enum 값 비교  ->  ");
  if((Port.etc1).equals(Port.etc2)) {
    System.out.println("enum 값이 동일하다.");
  }else {
    System.out.println("enum 값이 구분된다.");
    if((Port.etc1.portNumber) == (Port.etc2.portNumber)) {
      System.out.println("enum 값이 구분되지만 그 안의 속성은 중복될 수 있다.");
    }			
  }
  System.out.print("다른 enum, 같은 이름은  -> ");
  if((Port.etc1).equals(Fruit.etc1)) {
    System.out.println("구분되지 않는다.");
  }else {
    System.out.println("구분된다.");
  }
}
```
> 결과
```
enum 값 확인
http 80
https 443
ftp 20
ssh 22
etc1 0
etc2 0
enum 값 비교  ->  enum 값이 구분된다.
enum 값이 구분되지만 그 안의 속성은 중복될 수 있다.
다른 enum, 같은 이름은  -> 구분된다.
```
### 인스턴스 변수와 생성자.
#### enum 클래스의 생성자의 특징
1. 생성자를 이용하여 추가 속성을 부여할 수 있다.  
2. private 접근 제어자를 사용해야한다.
   - enum 타입은 컴파일 시점에 모든 값을 알고 있어야한다.(전역적으로 쓰이기 때문, static) 따라서 인스턴스 생성이 불가능하다.
   - 이 이유로 private설정을 해야하며 final 제어자와 동일하게 쓰인다.  
enum은 인스턴스 생성 및 상속이 불가능하며 싱글톤을 만들 수 있다.

## enum이 제공하는 메소드 (values()와 valueOf())
- value()   
열거형의 모든 상수를 배열에 담아서 반환한다.  
컴파일러에서 자동으로 추가된 것.
- ordinal()  
enum에서 정의된 순서대로 정수를 반환하는 것.  
 - valueOf()  
열거형 상수를 이름으로 문자열 상수에 대한 참조를 얻을 수 있다.
- name()
enum으로 정의된 것을 가져옴.
```java
public enum Phone {
    iPhone("A")
    , Galaxy("S")
    ;

    private String company;
    
    Phone(String status) {
        this.company = status;
    }
    public String getCompany() {
        return company;
    }
}
```
> 테스트 코드
```java
import java.util.Arrays;

public class EnumTest {
    public static void main(String[] args) {

        System.out.println("iPhone(\"A\") Galaxy(\"S\")\n");
        System.out.println(String.format("Phone.iPhone.getCompany() : %s ", Phone.iPhone.getCompany()));
        System.out.println(String.format("Phone.Galaxy.getCompany() : %s ", Phone.Galaxy.getCompany()));

        System.out.println(String.format("Phone.values() : %s", Arrays.stream(Phone.values()).toList()));
        System.out.println(String.format("Phone.iPhone.name() : %s", Phone.iPhone.name()));
        System.out.println(String.format("Phone.Galaxy.ordinal() : %s ", Phone.Galaxy.ordinal()));
        System.out.println(String.format("Phone.iPhone.toString() : %s ",Phone.iPhone.toString()));
    }
}
```
> 결과
```
iPhone("A") Galaxy("S")

Phone.iPhone.getCompany() : A 
Phone.Galaxy.getCompany() : S 
Phone.values() : [iPhone, Galaxy]
Phone.iPhone.name() : iPhone
Phone.Galaxy.ordinal() : 1 
Phone.iPhone.toString() : iPhone 
```
-> 기본적으로 enum은 선언된 숫자 다뤄지는 것 같고 값을 넣으려면 생성자를 이용해야하며,  
그 값을 가져오기 위해서 getter를 사용해야하는 것 같다.
## java.lang.Enum
enum을 사용하기 위해 조상이 되는 클래스.  
자동으로 상속받음.
## EnumSet
enum 클래스로 작동하기 위해 특화된 set collection.
### 특징
- null이 불가능하다.
- 동일한 열거형 값만 포함할 수 있다.
- 쓰레드로부터 안전하지 않고, 필요에 따라 외부에서 동기화한다.
### 구현해보기
> 코드
```
public static void main(String args[]) throws Exception {
  EnumSet<Port> set = EnumSet.allOf(Port.class);
  System.out.println("전체 출력");
  set.forEach(System.out::println);
  System.out.println("포함될 요소 입력하기");
  set = EnumSet.of(Port.etc1, Port.etc2);
  set.forEach(System.out::println);
  System.out.println("제거할 요소 입력하기");
  set = EnumSet.complementOf(EnumSet.of(Port.etc1, Port.etc2));
  set.forEach(System.out::println);
}
```
> 결과
```
전체 출력
http
https
ftp
ssh
etc1
etc2
포함될 요소 입력하기
etc1
etc2
제거할 요소 입력하기
http
https
ftp
ssh
```
#### 
== 사용 가능/ equals 사용 불가  
\>,< 사용 불가/ compareTo 사용 가능 -> comparable을 inmplement함.
 
 ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
