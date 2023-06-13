# 3주차 과제: 연산자
> 자바가 제공하는 다양한 연산자를 학습하세요.

## 학습 목록
- [산술 연산자](#산술-연산자)
- [비트 연산자](#비트-연산자)
- [관계 연산자](#관계-연산자)
- [논리 연산자](#논리-연산자)
- [instanceof](#instanceof)
- [assignment(=) operator](#assignment(=)-operator)
- [화살표(->) 연산자](#화살표(->)-연산자)
- [3항 연산자](#3항-연산자)
- [연산자 우선 순위](#연산자-우선-순위)
- [(optional) Java 13. switch 연산자](#(optional)-Java-13.-switch-연산자)

## 산술 연산자
+, -, *, //, %와 같은 연산자  
%를 제외히고는 수학에서 연산하는 것과 동일하다  
%는 나머지 연산자로 나눗셈을 계산한 후 그 나머지 값을 알 수 있는 연산자이다.  
## 비트 연산자 
& | ^  
비트단위로 연산하는 연산자  
& : AND  
| : OR   
^ : XOR, 두 값이 서로 달라야 true  
~ : switch, 전환 연산자. 부호가 바뀐다.  
## 관계 연산자 
피연산자 간의 관계를 나타내는 연산자로 수학의 연산자와 동일  
피연산자간의 사이의 자료형이 다를 경우 자료형의 범위가 튼 쪽으로 자동 형 변환이 된다.  
문자열을 비교 할 떄는 equals()를 이용하여 비교한다.
문자열에서 *==을 쓰면 참조되는 주소를 비교한다.

## 논리 연산자 
&& : AND, 어느 한쪽이 false면 false.  
|| : OR, 어느 한쪽이 true면 true.  
! : NOT  
 - 논리연산자는 앞서있는 조건만으로 결과를 알 수 있을 때 뒤에 조건은 실행하지 않는다.  
예를 들면,  
&&의 경우 앞이 false면 더 확인하지 않고 false.
||의 경우 앞이 true면 더 확인하지 않고 true.  

## instanceof 
참조형태의 인스턴스의 자료형을 알려고 할떄 사용하는 연산자.  
boolean type이며 true일 경우 형변환이 가능하다.  
상속의 관계에서도 Parents가 부모 클래스 Child가 자식 클래스로 두 클래스는 상속관계에 있다고 할떄,   
```
Child c = new Child();  
Parents p = new Parents();
if(c.instanceof(p)){
//false?
}

if(p.instanceof(c)){
//true?
}
```
.... 모르겠다.
## assignment(=) operator 
좌변에 우변의 값을 넣어 사용하는 연산자.  
=을 사용하면 변수에 값을 넣을 수가 있다.  
```
int x;
x = 3;//x에 3이라는 값을 넣을 수 있다.

```
## 화살표(->) 연산자 
- 람다식을 표현하기 위해 사용
- 익명 객체를 나타낸다
## 3항 연산자 
조건문? true인 경우 실행 : false일 경우 실행   
if문 없이 조건문의 수행이 가능함.
```
int x = 3;
int y = 2;
int bigger = x > y ? x :  y;
System.out.println(bigger);//큰 수가 출력됨.
```
## 연산자 우선 순위 
1. 증감 연산자 ++. --  
2. 산술 연산자   
3. 관계 연산자 > < <= >= instanceOf  
4. 논리 연산자  ^ ! | && || 
5. 삼항 연산자 > :
6. 대입 연산자 = 
## (optional) Java 13. switch 연산자 

##
``
int start = 0;
int end = 1;

int result = (start+end) / 2; //overflow가 생길 수 있다.
//왜냐면 int + int > int 자료형 범위를 벗어날 수 있기에!!!
int start = 21억 이상
int end = 21억 이상
int result = (start+end) / 2; //이상한 값이 나온다.
overflow의 값을 나누기에 값이 이상해짐.
//안전하게 나누기.
int result = start +(end - start) /2
int result = (start + end) >>>1;//음수는 안 됨.
``
   ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
