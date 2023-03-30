# java.lang 패키지
> 
## 목록
- [java.lang 패키지](#javalang-패키지)
  - [목록](#목록)
  - [java.lang 패키지](#javalang-패키지-1)
  - [class Object](#class-object)
    - [equals()](#equals)
    - [hashCode()](#hashcode)
  - [Class Byte](#class-byte)
  - [Class Math](#class-math)
  - [Class String](#class-string)
    - [equals()](#equals-1)
    - [hashCode()](#hashcode-1)
  - [참고 문헌](#참고-문헌)
## java.lang 패키지
> 
java.lang 패키지는 자바 프로그래밍시 기본이 되는 클래스들을 포함하고 있다.  
따라서 패키지임에도 import 없이 사용할 수 있는데,  
대표적으로 우리가 System.out.print() 사용시 System 패키지를 import하지 않고,  
String 타입의 문자를 비교하기 위해 equals()를 사용할 때, import하지 않고 쓸 수 있는 것을 의미한다.  

## class Object
Object 클래스는 모든 클래스의 조상클래스로 암묵적으로 상속되어있는 상태로 모든 클래스에서 바로 사용 가능하다.  
ex ) equals(Object obj), toString()
### equals()
- 객체 참조에 의해 **동등**한지를 확인함.
- hashCode를 기반으로 동등성을 확인한다. 따라서, override하게 되면 hashCode도 overide해야함.

### hashCode()
- 객체의 내부 주소를 해시 코드 값으로 변환하여 리턴하는 것.
- 단, 두 번 호출하면 값은 달라짐. 값이 유지될 필요가 없음.

## Class Byte
Number를 extends 받아서 상수 선언시 유용하게 사용 가능하다.
ex ) MAX_VALUE, MIN_VALUE, SIZE

## Class Math
수학 계산과 관련있는 메서드들을 갖고 있다.  
ex ) min(a, b), max(a, b)

## Class String
### equals()
- 2개의 객체가 동일한지 문자열 하나하나를 비교한다.
- **==** 을 이용하여 비교하게 되면 메모리의 위치를 비교하게 되어 실제 객체가 가지는 값이 같더라도 다르다는 결과가 나올 수 있어서 실제 값을 알기 위해 사용하는 함수.
### hashCode()
- string을 hashcode로 변환하여 사용
- 계산 로직

  ```
   s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
   ```

## 참고 문헌
- https://docs.oracle.com/javase/8/docs/api/
- 남궁 성, Java의 정석, 도우출판
