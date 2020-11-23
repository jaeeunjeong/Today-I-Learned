# 2주차 과제: 자바 데이터 타입, 변수 그리고 배열 
> 자바의 프리미티브 타입, 변수 그리고 배열을 사용하는 방법을 익힙니다.

## 학습 목록
- [프리미티브 타입 종류와 값의 범위 그리고 기본 값](#프리미티브-타입-종류와-값의-범위-그리고-기본-값)
- [프리미티브 타입과 레퍼런스 타입](#프리미티브-타입과-레퍼런스-타입)
- [리터럴](#리터럴)
- [변수 선언 및 초기화하는 방법](#변수-선언-및-초기화하는-방법)
- [변수의 스코프와 라이프타임](#변수의-스코프와-라이프타임)
- [타입 변환, 캐스팅 그리고 타입 프로모션](#타입-변환,-캐스팅-그리고-타입-프로모션)
- [1차 및 2차 배열 선언하기](#1차-및-2차-배열-선언하기)
- [타입 추론, var](#타입-추론,-var)

## 프리미티브 타입 종류와 값의 범위 그리고 기본 값
Primitive Type : 변수가 값에 의해 결정되는 것  
int  
char  
long  
double  
...  
부호비트때문에 2^자리수-1만큼의 범위를 가진다.  
## 프리미티브 타입과 레퍼런스 타입
Reference Type : 주소를 가진 변수 타입.  
변수에 주소가 있고 그 주소를 호출하는 방식으로 변수를 사용한다.  
따라서 변수가 저장되는 곳과 실제 값이 저장되는 곳이 달라진다.  
**주소가 저장되는 곳은  Runtime Data Area의 stack이고  
값이 저장되는 곳은 heap이다.**  
ex) 클래스이름 변수이름;  

primitive Type이 저장되는 곳은 Runtime Data Area에서 Stack 영역.  
referenct Type이 저장되는 곳은 주소부분은 stack이고, 실제 값은 heap이다.  

## 리터럴
상수 : final로 선언되어 값의 변환이 불가능한 변수를 말함  
```final int MAX_VALUE = 10000;  ```  
리터럴 : 변수를 초기화할 때 오른쪽 부분으로 값 자체를 말함.  
## 변수 선언 및 초기화하는 방법 
선언하는 방법  
```
DataType DataName;  

int nbr;  
String str;  
```
초기화하는 방법  
-> 초기화란, 선언된 변수에 값을 넣어주는 것.  
```
int nbr;
nbr = 3;
String str = "abc";
```

_변수별 다른 부분을 알아보고자 바이트코드를 확인해보았지만 잘 모르겠다_
```
class Main{
  static int MAX = 1234567;
  public static void main(String[] args) { 
    int nbr = 1;
    String str = "Hello";
  }
}
```

```
javap -c Main.class
Compiled from "Main.java"
class Main {
  static int MAX;

  Main();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: iconst_1
       1: istore_1
       2: ldc           #2                  // String Hello
       4: astore_2
       5: return

  static {};
    Code:
       0: ldc           #3                  // int 1234567
       2: putstatic     #4                  // Field MAX:I
       5: return
}
```


## 변수의 스코프와 라이프타임  
변수는 선언된 위치에 따라 Scope(존재하는 메모리 위치)가 달라진다.  
### Class 변수
 - 정의 : static이 붙은 변수로 JVM의 method에 생성
 - Life Time:  
### Local 변수
 - 정의 : 클래스 안에 존재하는 변수 
 - Life Time: 호출된 클래스나 범위의 종료와 함께 소멸됨.  
### Instance 변수 
 - 정의 : 이미 만들어진 변수를 사용하기 위해 JVM의 heap에 올려져서 사용됨.  
 - Life Time: 인스턴스가 생성되고 메모리에서 사라질 때까지.  


## 타입 변환, 캐스팅 그리고 타입 프로모션
   ### 타입 변환
   String으로 바꾸려면 String.valueOf()를 사용하면 됨
   ```
   int a = 1;// Integer 형태
   String aa = String.valueOf(a);
   ```
   String을 Primitive로 바꾸려면 Integer.parseInt()와 같은 방법 사용.  
   
   
   ### Casting (강제 형변환)
   ```
   int a = 123;
   long b = (long)a;
   ```
   ### Promotion (자동 형변환)
   ```
   float a = 123;
   ```
   위의 경우에 123은 int type이지 float가 아니다.
   ```
   float a = (float)123;
   ```
   위와 같은 형태로 자동 형변환이 된다.
## 1차 및 2차 배열 선언하기  
1차원 배열  
 ```
 int []arr = {1,2,3};
 ```
2차원 배열  
 ```
 int [][]arr = new int[3][2];
 arr[0][0] = 0;  
 arr[0][1] = 1;  
 ```
 
## 타입 추론, var
 - 타입 추론  
 데이터 타입을 명시하지 않고 컴파일러가 데이터 타입을 추론하여 사용하는 것.  
 var 를 사용함.
 - var  
 로컬에서만 사용가능함.  
 선언과 동시에 초기화 해야함.  
 데이터 타입을 분명하게 명시하지 않아도 사용가능함.  
 ##### 사용 예시  
 foreach   
 lambda  
 익명 클래스  
 _제네릭과 관련된 부분도 있음(추후 덧붙이기.)_

  
   ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
  
