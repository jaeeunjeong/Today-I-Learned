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
  ~~~
  class Main{
    public static void main(String[] args) { 
        System.out.println("Hello World");
    }
  }
  ~~~
  
  javac로 compile
  ~~~
  javac Main.java
  ~~~
  Main.java/Main.class 생성
  
## 실행하는 방법
  컴파일된 프로그램을 JVM위에 Load해서 java.exe가 실행!  
  ~~~
  java Main
  ~~~
## 바이트코드란 무엇인가
  JVM 이해할 수 있는 코드  
  .class파일 안에 있는 코드  
  compile된 Java code  
  JVM은 바이트코드를 해석하여 프로그램이 실행되기 때문에 OS에 종속되지 않는 것.  
  ### 컴파일된 class를 바이트코드로 확인해보기
  ~~~
  javap -c Main.class
  ~~~  
  ~~~
Compiled from "Main.java"
class Main {
  Main();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3                  // String Hello World
       5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}
~~~
## JIT 컴파일러란 무엇이며 어떻게 동작하는가
JIT(Just In Time)
  bytecode를 기계어로 바로 변환해 주는 코드.   
  인터프리터와 같이 동작함. (interpreter은 하나 하나 해석하는 방식)  
  single thread처럼 사용됨.  
  자주 사용되는 기계어로 생각되면 caching해 두었다가 사용함.   
  class -> interpreter가 아니라 class-> 기계어 방식  
  빈번하게 사용되는 클래스라면 cache에 저장해두었다가, 꺼내써서 빠르게 사용 가능하도록 해준다.
## JVM 구성 요소
  ### Class Loader  
   - .java를 실제로 사용하기 위해 jvm에 로드 하는 것.
   - bytecode를 읽어서 merory에 적절하게 배치. 
   - Loading->Link->initialization  
   #### 클래스 로딩 과정
   ##### Loading
   클래스 로더가 JRE 라이브러리에서 해당 클래스의 유무를 조사한다.  
   만약 클래스를 발견 못 했다면? JDK 확장 디렉토리에서 해당 클래스의 유무를 조사한다.  
   JDK 확장 디렉토리에서도 클래스를 발견하지 못했다면? class path 환경 변수에 지정된 디렉토리에서 해당 클래스의 유무를 조사한다.  
   =>JRE 라이브러리 >  JDK 확장 디렉토리 > class path 환경 변수에 지정된 디렉토리 등은 상속 관계에 있다.  
   따라서 위임 방식으로 클래스를 발견한다.
   ##### Linking
   ###### verify
   해당 클래스 파일이 올바를 바이트코드인지 검증한다.
   올바른 바이트코드라면  method 영역으로 파일을 로딩한다.
   ###### prepare
   클래스 변수를 만들어야한다면 method에 변수를 준비한다.
   ##### Initialization
  ### Runtime Data Areas
   - JVM이 OS위에 올라가서 실행될때, OS가 할당해주는 메모리 영역  
   - 자바는 runtime에 메모리가 할당되기에 이 위치에 있는 클래스들은 실행될 클래스들!
   - heap, stack area, method area, native method stack, PC register for thread  
   #### Method Area
   JVM 당 하나만 존재하며, 모든 Thread가 공유하는 영역이다.  
   프로그램에서 사용되는 클래스 변수, 클래스 메서드가 저장되는 영역이다  
   #### Heap Area
   JVM 당 하나만 존재하기 때문에 Multi-Thread 환경에서 안전하지 않다.  
   프로그램에서 사용되는 모든 인스턴스 변수가 저장되는 영역이다.
   new를 통해 생성된 인스턴스 정보가 저장된다.  
   heap에 저장된 인스턴스 변수에는 인스턴스의 실제 주소가 저장되어있는데, 만약 주소가 가리키는 공간이 null이라면 에러가 발생한다.
   #### Stack
   프로그램이 호출되면 저장되는 영역.  
   각 Thread마다 개별적으로 영역을 할당받는다.
   #### PC Register
   실제 실행되는 Thread의 주소공간으로 Thread 시작시 생성되며 Thread가 실행되고 있는 명령어의 주소 정보가 있다.  
   #### Native Method Stack
   JNI를 이용해서 실행되는 부분으로 c/c++ 코드들이 실행되는 영역이다.  
  ### Execution Engeine 
  #### Interpreter/JIT/GC 등이 존재.
   - Interpreter : Bytecode를 읽어서 기계어(Native Code)로 변환.  
   - JIT : 중복된 Bytecode가 사용되면 JIT에서 찾아서 사용. Cache 느낌.  
   - Garbage Collector(GC) : 참조되지 않는 메모리를 제거하는 작업을 하는 등 메모리 관리 시스템.  
## JDK와 JRE의 차이
  JRE(Java Runtime Environment) = JVM + 주요 Library  
  JDK(Java SE Development Kit) = JRE + Development Tools and Utilities  
 
 
 
 ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
  https://d2.naver.com/helloworld/1230  
  https://yadon079.github.io/
