# 1주차 과제: JVM은 무엇이며 자바 코드는 어떻게 실행하는 것인가.
> 자바 소스 파일(.java)을 JVM으로 실행하는 과정 이해하기.

## 학습 목록
- [JVM이란 무엇인가](#JVM이란-무엇인가)
- [컴파일 하는 방법](#컴파일-하는-방법)
- [실행하는 방법](#실행하는-방법)
- [바이트코드란 무엇인가](#바이트코드란-무엇인가)
- [JIT 컴파일러란 무엇이며 어떻게 동작하는가](#JIT-컴파일러란-무엇이며-어떻게-동작하는가)
- [JVM 구성 요소](#JVM-구성-요소)
- [JDK와 JRE의 차이](#JDK와-JRE의-차이)

## JVM이란 무엇인가
  Java Virtual Machine  
  Java가 실행되는 머신을 말한다.  
  JVM위에서 Java가 실행되기 때문에 Java는 OS에 자유로운 성질을 지닌다.  
## 컴파일 하는 방법
  Java Compiler(javac)에서 Compile됨.  
  (.java를 .class로 변환하는 과정)  
## 실행하는 방법
  컴파일된 프로그램을 JVM위에 Load해서 java.exe가 실행!  
## 바이트코드란 무엇인가
  JVM 이해할 수 있는 코드  
  .class파일 안에 있는 코드  
  compile된 Java code  
  JVM은 바이트코드를 해석하여 프로그램이 실행되기 때문에 OS에 종속되지 않는 것.  
## JIT 컴파일러란 무엇이며 어떻게 동작하는가
  bytecode를 기계어로 바로 변환해 주는 코드. 
  인터프리터와 같이 동작함.(interpreter은 하나 하나 해석하는 방식)  
  single thread처럼 사용됨.  
  자주 사용되는 기계어로 생각되면 caching해 두었다가 사용함.   
## JVM 구성 요소
  Class Loader  
  Runtime Data Areas  
  Execution Engeine(GC가 있는 곳)  
## JDK와 JRE의 차이
  JRE(Java Runtime Environment) = JVM + 주요 Library  
  JDK(Java SE Development Kit) = JRE + Development Tools and Utilities  
