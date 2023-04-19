# 7주차 과제: 패키지
> 자바의 패키지에 대해 학습하세요.

## 학습 목록 
- [7주차 과제: 패키지](#7주차-과제-패키지)
  - [학습 목록](#학습-목록)
  - [package 키워드](#package-키워드)
    - [package의 선언](#package의-선언)
    - [패키지 명명 규칙](#패키지-명명-규칙)
  - [import 키워드](#import-키워드)
  - [클래스패스](#클래스패스)
  - [CLASSPATH 환경변수](#classpath-환경변수)
  - [-classpath 옵션](#-classpath-옵션)
  - [접근지시자](#접근지시자)
    - [참고 자료](#참고-자료)

## package 키워드
- class의 집합  
- Interface나 class들을 포함시킬 수 있고 관련있는 것끼리 묶음으로써 효율적인 관리가 가능하다.  
- directory의 개념.  
### package의 선언  
```
package 패키지 이름;
```
### 패키지 명명 규칙
 - class와의 구분을 위해 package는 소문자로 이름을 짓는다.  
 - 자바 예약어를 사용하지 않는다.

## import 키워드
다른 패키지를 사용하고자 할때 쓰는 키워드.  
다른 패키지에 있는 어떠한 클래스를 사용하려고 할 때, 일일히 클래스를 넣는 일은 번거롭기에, import를 이용하여 패키지 째로 다른 패키지를 넣어서 사용이 가능하다.
```
package ~;

//1. 클래스 이름을 지정하여 패키지명을 등록하는 방법.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

//2. *(ALL)로 패키지에 속하는 클래스 전체를 등록하는 방법
import java.util.*;
```
실행상의 큰 성능차이는 없다.
## 클래스패스
컴파일러(javac.exe)나 JVM등이 클래스의 위치를 찾는데 사용되는 경로.
경로에는 *패키지의 루트 디렉토리*가 있다.

## CLASSPATH 환경변수
CLASSPATH라는 변수를 windows기준 제어판 - 시스템 - 고급 시스템 설정 - 환경 변수 - 새로 만들기를 통해 등록해준다.

## -classpath 옵션
## 접근지시자
|   | |
|:-------:| :----------- |
|public  | 접근에 대한 제한이 없음.|
|protected | 같은 패키지내 + 다른 패키지의 자손 클래스에서 접근 가능.|
|private | 같은 클래스에서만 접근 가능.|
|default | 같은 패키지내에서만 접근 가능|

||같은 클래스 |같은 패키지|상속 받은 클래스| 전체 |
|:---|:---:|:---:|:---:|:---:|
|public|O   |O  |O  |O  |
|protected|O    |O  |O  |X  |
|default|O  |O  |X  |X  |
|private|O  |X  |X  |X  |

데이터가 유효한 값을 유지하고, 외부로부터 쉽게 값을 변경함을 막기 위해서, 외부로부터의 접근을 제한하기 위해 등장.
객체지향의 캡슐화에 해당하는 개념

   ### 참고 자료  
남궁 성, Java의 정석, 도우출판  
