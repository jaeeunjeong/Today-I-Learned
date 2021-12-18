# java.lang 패키지
> 
## 목록
 - [java.lang 패키지](##java.lang_패키지)
 - [class Object](#Class_Object)
 - [Class Byte](#Class_Byte)
 - [Class Math](#class_Math)
## java.lang 패키지
> 
java.lang 패키지는 자바 프로그래밍시 기본이 되는 클래스들을 포함하고 있다.  
따라서 패키지임에도 import 없이 사용할 수 있는데,  
대표적으로 우리가 System.out.print() 사용시 System 패키지를 import하지 않고,  
String 타입의 문자를 비교하기 위해 equals()를 사용할 때, import하지 않고 쓸 수 있는 것을 의미한다.  

## class Object
Object 클래스는 모든 클래스의 조상클래스로 암묵적으로 상속되어있는 상태로 모든 클래스에서 바로 사용 가능하다.  
ex ) equals(Object obj), toString()

## Class Byte
Number를 extends 받아서 상수 선언시 유용하게 사용 가능하다.
ex ) MAX_VALUE, MIN_VALUE, SIZE

## Class Math
수학 계산과 관련있는 메서드들을 갖고 있다.  
ex ) min(a, b), max(a, b)

## 참고 문헌
- https://docs.oracle.com/javase/8/docs/api/
- 남궁 성, Java의 정석, 도우출판
