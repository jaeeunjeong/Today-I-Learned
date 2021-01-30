
# 11주차 과제: Enum 
> 자바의 열거형에 대해 학습하세요. 
## 학습 목록 
- [enum 정의하는 방법](#enum_정의하는_방법)
- [enum이 제공하는 메소드 (values()와 valueOf())](#enum이_제공하는_메소드_(values()와_valueOf()))
- [java.lang.Enum](#java.lang.Enum)
- [EnumSet](#EnumSet)

## enum 정의하는 방법
### enum이란
관련있는 상수들을 묶어서 그룹처럼 사용하는 자료형을 말한다.
Java에는 1.5부터 등장하였다.
### java의 enum의 특징
 - 상수는 값이 바뀌면 새로 컴파일 해야하지만 enum 은 다시 컴파일 하지 않아도 된다.
 - java에서는 타입까지 검증하기 때문에 **typesafe enum** 이라고 할 수 있다.
### enum의 정의
```
enum port{http, https, ftp, ssh};
port.http;//
```
### enum에 값 넣기
```
enum port{http(80), https(443), ftp(20), ssh(22)};
```
참조 할 떄는, static 변수를 참조하는 것과 동일히다.  
### 인스턴스 변수와 생성자.
지정된 값을 저장하기 위해 생성자를 사용해야하고, 묵시적으로 private하다.  
외부에서 값을 사용하기위해 getter을 사용하고, final 형태이다.

## enum이 제공하는 메소드 (values()와 valueOf())
- value()   
열거형의 모든 상수를 배열에 담아서 반환한다.  
컴파일러에서 자동으로 추가된 것.
- ordinal()  
enum에서 정의된 순서대로 정수를 반환하는 것.  
 - valueOf()  
열거형 상수를 이름으로 문자열 상수에 대한 참조를 얻을 수 있다.
## java.lang.Enum
enum을 사용하기 위해 조상이 되는 클래스.  
자동으로 상속받음.
## EnumSet

### 구현해보기
#### 
== 사용 가능/ equals 사용 불가  
\>,< 사용 불가/ compareTo 사용 가능 -> comparable을 inmplement함.
 
 ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
