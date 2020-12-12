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

## 반복문
### if문
```
if(조건문){ 
 조건에 맞을 시 수행할 내용
}
```
조건문에는 true/false 결과를 나타낼 것들로 들어간다.(ex. 비교/논리 연산자)
{} 뒤에는 ;를 붙이지 않는다.
{}는 없어도 되지만 바로 밑의 문장만 수행한다.
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

### if/elseif/else 문
처음 등장한 if가 false일 경우, 그 다음에 또 다른 조건을 이용하여 검증하고자 할 때,  
else if() 를 이용하여 추가 검증이 가능하다.

### switch~ case 문
하나의 조건식으로 여러개를 검증하고자 할 경우 사용  
검증할 결과는 정수 또는 문자열임.  
case문의 값은 정수, 상수만 가능하며 중복되지 않아야한다.
break 를 이용하여 가까운 case 조건을 벗어난다.
```
String input = Scanner.in();

switch(){
 case 변수
  break;
```
   ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
