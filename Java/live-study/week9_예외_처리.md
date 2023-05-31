# 9주차 과제: 예외 처리
> 자바의 예외 처리에 대해 학습하세요.

## 학습 목록 
- [9주차 과제: 예외 처리](#9주차-과제-예외-처리)
  - [학습 목록](#학습-목록)
  - [자바에서 예외 처리 방법 (try, catch, throw, throws, finally)](#자바에서-예외-처리-방법-try-catch-throw-throws-finally)
    - [try ~ catch](#try--catch)
    - [throw](#throw)
    - [throws](#throws)
    - [try ~ catch ~ finally](#try--catch--finally)
    - [try ~ with ~ resources](#try--with--resources)
  - [자바가 제공하는 예외 계층 구조](#자바가-제공하는-예외-계층-구조)
  - [Exception과 Error의 차이는?](#exception과-error의-차이는)
  - [RuntimeException과 RE가 아닌 것의 차이는?](#runtimeexception과-re가-아닌-것의-차이는)
  - [커스텀한 예외 만드는 방법](#커스텀한-예외-만드는-방법)
    - [참고 자료](#참고-자료)

## 자바에서 예외 처리 방법 (try, catch, throw, throws, finally)

### try ~ catch
catch에 선언된 오류로 try 괄호 안에 있는 부분을 검증한다.  
오류가 있다 -> catch문 수행하고 프로그램 종료  
오류가 없다 -> catch문을 수행하지 않고 그냥 지나간다.  
해당하는 부분에 오류가 있지만 catch에 해당하는 오류가 아닌 경우 -> catch를 수행하지않고 그냥 지나간다.  
```
try{

}catch(Exception e){

}
```
*여러개의 catch를 쓴다면, 순서대로 쓰는 것이 중요하다. 작은 부분(구체적)에서 큰 부분(포괄적)으로 넓어지듯 써야한다. -> 컴파일에러*
### throw
고의로 예외를 발생시킬 때 사용.  
예외처리를 해당 클래스에서 하는 것이 아니라 호출한 곳에다가 떠넘긴다.
```
public void main(String[] args)) {
   int N = 0;
   if(N == 0) throw new NullPointerException();
}
```

### throws
**선언부**에 적어서 오류 처리를 다른 메서드에 떠넘기기  
```
public static void main(String[] args) throws IOException {
   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   StringTokenizer st;
}
```
### try ~ catch ~ finally
try - catch문과 함께 예외 발생여부에 상관없이 실행되어야할 코드를 포함시킬 목적으로 사용.  
```
try{

}catch(Exception e){

}finally{

}
```
### try ~ with ~ resources
- 자바 8에서는 이것을 쓰자.  
- 코드도 줄고 훨씬 안전하다.  
- closable을 상속받는 것에 한해!!
- finally block을 줄임.
- cf 바이트 코드를 확인해보자 신기한 부분이 많음!
- catch(throwable e)을 사용해서 finally를 사용 가능하다.

*finally에 return을 사용하는 거는 anti-pattern*
## 자바가 제공하는 예외 계층 구조
![structure](https://github.com/jaeeunjeong/Today-I-Learned/blob/master/Java/live-study/%EC%9E%90%EB%B0%94%EA%B0%80%20%EC%A0%9C%EA%B3%B5%ED%95%98%EB%8A%94%20%EC%98%88%EC%99%B8%20%EA%B3%84%EC%B8%B5%20%EA%B5%AC%EC%A1%B0.JPG)
## Exception과 Error의 차이는?
둘다 프로그램의 실행에 있어 장애가 된다는 점은 동일하지만,  
*Error*은 프로그래머가 수습 불가능한 장애  
*Exception*은 프로그래머가 수습 가능한 장애를 말한다.

## RuntimeException과 RE가 아닌 것의 차이는?
Exception은 크게 두 가지로 나눌 수 있다.  
RuntimeException과 RuntimeException이 아닌 것.  
|-| RuntimeException | 그 외|  
|--------|----|----|
에러 발생 시점 | Runtime (실행 중)| 컴파일 시점
에러 발생 원인 | 프로그래머의 실수| 사용자 사용에 의해 에러 발생 
예외 처리 유무 | 안해도 됨. **unchecked**| 무조건 해야함 **checked**
에러 예시 | IndexOutOfEception, NullPointException| FileNotFoindException, DataFormatException

## 커스텀한 예외 만드는 방법
사용자가 예외를 만들 수 있는데, exception을 오버라이드 해서 사용가능하고, 필요에 따라 예외 클래스를 만들 수도 있음.
```
public class AccessDeniedException extends RuntimeException{
}
```
- 예외를 생성해서 만들면 어떤 에러가 발생한건지 명시적으로 알 수 있다.
 - 4가지 best practices
  1. Java의 표준 예외와 비교하여 특징적으로 구분되어 개발상의 이점을 제공하는 경우에만 구현해야 한다.
  2. 명명 규칙 따르기 
    1. postfix가 Exception으로 명명하기.
  3. 예외 클래스에 대한 Javadoc 주석 제공
    1. 목적에 맞게 사용할 수 있도록 기록을 남길 것.
  4. 원인을 설정하는 생성자 제공
    1. 표준 예외가 발생하면 그 예외를 보여주기.
### 참고 자료  
- 남궁 성, Java의 정석, 도우출판  
- https://dzone.com/articles/implementing-custom-exceptions-in-java?fromrel=true
