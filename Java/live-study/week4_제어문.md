# 4주차 과제: 제어문
> 자바가 제공하는 제어문을 학습하세요.

## 학습 목록
- [선택문](#선택문)
- [반복문](#반복문)

## 과제 
### 과제 0. JUnit 5 학습하세요.
- 인텔리J, 이클립스, VS Code에서 JUnit 5로 테스트 코드 작성하는 방법에 익숙해 질 것.
 - 이미 JUnit 알고 계신분들은 다른 것 아무거나!
 - 더 자바, 테스트 강의도 있으니 참고하세요~
### 과제 1. live-study 대시 보드를 만드는 코드를 작성하세요.
 - 깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
 - 참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
 - Github 자바 라이브러리를 사용하면 편리합니다.
 - 깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.
### 과제 2. LinkedList를 구현하세요.
 - LinkedList에 대해 공부하세요.
 - 정수를 저장하는 ListNode 클래스를 구현하세요.
 - ListNode add(ListNode head, ListNode nodeToAdd, int position)를 구현하세요.
 - ListNode remove(ListNode head, int positionToRemove)를 구현하세요.
 - boolean contains(ListNode head, ListNode nodeTocheck)를 구현하세요.
### 과제 3. Stack을 구현하세요.
 - int 배열을 사용해서 정수를 저장하는 Stack을 구현하세요.
 - void push(int data)를 구현하세요.
 - int pop()을 구현하세요.
### 과제 4. 앞서 만든 ListNode를 사용해서 Stack을 구현하세요.
 - ListNode head를 가지고 있는 ListNodeStack 클래스를 구현하세요.
 - void push(int data)를 구현하세요.
 - int pop()을 구현하세요.
### 과제 5. Queue를 구현하세요.
 - 배열을 사용해서 한번
 - ListNode를 사용해서 한번.

## 선택문
### if 문
로직 상에서 일정 조건에 따라 흐름을 다르게 하고 싶을 때 사용한다.
```
if(조건문){ 
 조건에 맞을 시 수행할 내용
}
```
조건문에는 boolean Type이 들어간다. (ex. 비교/논리 연산자)  
**중괄호 {}** 뒤에는 **;** 를 붙이지 않는다.  
**중괄호 {}** 는 없어도 되지만 바로 밑의 문장만 수행한다.  
```
if(false) System.out.println("Hello");
System.out.println("Bye");
```
출력
```
Bye
```

### if/else문
if가 false일 경우 실행될 부분을 else를 이용하여 표현한다.
```
int a = 100;
int b = 3;
    
if(b != 0) a /= b; //b는 0이 아닌 경우
if(b == 0) a = 0; // b는 0인 경우
   
System.out.println(a);
```
```
if(b != 0) a /= b; //b는 0이 아닌 경우
else a = 0; // b는 0인 경우
    
System.out.println(a);
```

두 소스는 같은 내용을 출력한다. 

### if/elseif/else 문
처음 등장한 if가 false일 경우, 그다음에 또 다른 조건을 이용하여 검증하고자 할 때,  
else if()를 이용하여 추가 검증이 가능하다.
```
int a = 100;
int b = 3;
    
if(b > 0) a *=b;
else if(b < 0) b *= b;
else a = b;
```
### switch~ case 문
하나의 조건식으로 여러 개를 검증하고자 할 경우 사용  
if 문으로의 여러 개 나열을 통해서도 표현할 수 있지만, switch 문을 사용하면 좀 더 간결한 코딩이 가능하다.  
검증할 결과는 정수 또는 문자열이다.  
case문의 값은 정수, 상수만 가능하며 중복되지 않아야 한다.  
#### break
**break** 를 만나면 가까운 case 조건을 벗어난다.
조건문 이외에도 반복문에서도 사용 가능하다.  
반복문에서 break를 사용하면 반복문의 변수가 종료 조건에 부합하지 않아도 종료된다.  
```
String input = Scanner.in();

switch(){
 case 변수
  break;
```
**조건문 안에는 들여쓰기를 통해 가독성을 높이자.**

## 반복문
### for문
분명한 반복 횟수를 알고 있을 때 유용.
```
for(변수의 초기화; 반복문 종료 조건; 변수의 증감){
 //반복할 내용
}
```
변수가 여러 개거나 종료 조건이 여러 개면 **,** 를 이용하여 복수 처리가 가능하다.  
변수는 for문 밖에서 선언해도 된다.
변수의 증감도 반복할 내용 부분에서 해도 된다.

```
int i = 1; // for 문 밖에서 변수를 선언
for(i = 0; i < 5;){
 i++; //증감이 반복 부분에서 수행 가능.
 i+=2; //대입 연산자를 사용하여도 됨.
}
```

#### for문 작동 방식
1. 변수를 초기화하여 시작점을 잡는다.  
2. 반복문 종료 조건에 맞는지 검증한다.  
 1) false일 경우 -> 반복문을 벗어난다.  
 2) true일 경우  
  a) 반복문 안의 내용을 수행한다.  
  b) 변수의 증감 부분으로 이동하여 변수에 변화를 준다.  
  c) 2.로 가서 반복을 수행한다.  
#### for 문에 초기화, 종료 조건, 변수의 증감은 생략할 수 있다.
while 문과 같게 행동한다.  
따라서 break를 사용하여 무한 루프에 걸리지 않도록 해야 한다.

#### for문에는 별칭을 정할 수 있다.
for 앞에 **지어줄 이름** 을 통해 별명을 지어줄 수 있다.  
이중 for 문 사용 시에 유용하게 사용할 수 있는데,  
```
outer: for(int i = 3; ;i++) {
		  for(int j = 1; j < 5; j++) {
    			System.out.println(i * j);
    			if(i *j > 10) break outer;
    }
}
```
이런 식으로 문법을 구현하면 아래와 같다.  
출력
```
3
6
9
12
```
이중 for 문을 한꺼번에 종료시킬 수 있어 유용하다.
#### 향상된 for 문
배열이나 collection framework과 같이 자료형이 연결지어 있는 경우 편리하게 이용할 수 있다.  
```
int[] arr = {1,2,3,4,5};
for(int number : arr) System.out.println(number);
```
출력
```
1
2
3
4
5
```
##### for 문의 반복문 종료 조건을 Collection을 이용하면 size를 이용하는 경우가 많은데, 그럴 때 변수를 따로 선언하여 사용하는 것이 유용하다.
```
List list = new ArrayList();

for(int i = 0; i < list.size(); i++){

}

int size = list.size();
for(int i = 0; i < size; i++){
 //더 유용한 방법.
}
```
### while 문
최초 한 번의 수행을 보장하는 반복문  
조건문의 내용이 false일 때까지 사용한다.
```
while(조건문){
 //반복할 내용
}

```
### do-while문
```
do{
}while();
```
while 문 앞에 꼭 실행되어야 하는 부분이 있을 때 사용한다.

## continue
**continue** 를 만나면 반복문의 끝으로 이동하여, 다음 반복으로 넘어가서 조건을 검증한다.  
continue 앞에 조건식을 주면 그 해당 조건문이 true일 경우 continue를 통해 반복문의 탈출이 가능하다.  

  

### 참고 자료  
남궁 성, Java의 정석, 도우출판  
https://yadon079.github.io/  
