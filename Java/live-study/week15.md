# 15주차 과제: 람다식
> 자바의 람다식에 대해 학습하세요.

## 학습 목록 
람다식 사용법
함수형 인터페이스
Variable Capture
메소드, 생성자 레퍼런스

## 람다식 사용법
### 람다식란?
일반 함수 형태를 화살표를 이용하여 하나의 식으로 간략하게 나타낸 것을 의미한다.  
람다식은 실제로는 익명 객체이다.
```
void sum(int a, int b){
    return a+b;
}
```

```
(int a, int b) -> return a+b; 
```
메서드의 이름과 반환타입을 제거하고 매개변수 선언부와 메서드 본체의 대괄호 {} 사이에 화살표를 추가하면 이와 같은 형태로 간단하게 나타낼 수 있다.  
또한 람다식에 선언된 매개변수가 충분히 추론 가능하다면 생략도 가능하다.  
### 주의 사항
- {} 안이나 return 이 아니라면 끝에 ;를 붙이지 않는다.
- 매개 변수가 하나라면 괄호를 생략할 수 있다.
## 함수형 인터페이스
람다식은 익명클래스 객체와 동등하다.
람다식를 호출하여 사용하기 위해선 참조변수가 필요하다.  
참조 변수는 클래스나 인터페이스를 사용한다.  
참조 변수의 타입은 람다식과 동등한 메서드가 정의되어 있어야한다.  
또한 람다식과 동일한 메서드를 정의해야만 참조변수로 익명 객체(람다식)의 메서드를 호출 할 수 있다.  
하나의 메서드가 선언된 인터페이스를 람다식으로 다루는 것은 어렵지 않고 자연스럽게 변환 될 수 있으며,  
인터페이스를 통해 람다식을 다루기로 결정되었다.  
람다식을 다루기 위한 인터페이스를 '함수형 인터페이스'라고 부르기로 했다.  
```
interface Calculate{
    public abstarct int sum(int left, int right);
}
```
위의 인터페이스를 구현한 익명 클래스는 아래와 같다.
```
Calculate c = new Calculate(){
    public abstarct int sum(int left, int right){
        return left+right;
    };
}
int result = c.sum(1,2);
```
이 부분을 람다식으로 바꾼다면,
```
Calculate c = (int left, int right) -> return left+right;

int result = c.sum(1,2);
```
위와 같다.  
이와 같이 익명클래스를 람다식으로 바꾸는 것이 매우 자연스러움을 알 수 있다.  
### 주의 사항
1. 함수형 인터페이스에는 오직 하나의 추상 메서드만 정의되어야 한다.  
그래야 람다식과 인터페이스 메서드가 1:1로 연결될 수 있다.  
0. @FunctionalInterface를 꼭 붙여주자.  
-> 컴파일러가 함수형 인터페이스를 올바르게 구현하였는지 확인해준다.
### 함수형 인터페이스 타입의 매개변수와 반환타입
```
@FunctionalInterface
interface DrawingInterface{
	void sketch();
}
```
추상 메서드가 선언된 인터페이스를 매개변수로서 사용한다면, 이 메서드를 호출할 떄 람다식을 참조하는 매개변수를 지정해주어야한다.  
따라서 람다식을 참조변수처럼도 사용가능한데, 이는 메서드를 이용해서 람다식을 주고 받을 수 있다는 뜻이다.
즉, 변수처럼 메서드를 다룰 수 있음을 의미한다.
```
@FunctionalInterface
interface DrawingInterface{
	void sketch();
}

class practice {
	static void execute(DrawingInterface drawingInterface) {
		drawingInterface.sketch();
	}
	
	static DrawingInterface getDrawingInterface() {
		DrawingInterface d = () -> System.out.println("LET'S SKETCH!!");
		return d;
	}
	
	public static void main(String[] args) {
		DrawingInterface d1 = () -> System.out.println("WARMING UP!");
		
		DrawingInterface d2 = new DrawingInterface() {
			
			@Override
			public void sketch() {
				// TODO Auto-generated method stub
				System.out.println("SETTING");
			}
		};
		
		DrawingInterface d3 = getDrawingInterface();
		
		d1.sketch();
		d2.sketch();
		d3.sketch();
		
		execute(d1);
		execute(() -> System.out.println("sketch()"));
	}
}
```

결과
```
WARMING UP!
SETTING
LET'S SKETCH!!
WARMING UP!
sketch()
```
## Variable Capture
## 메소드, 생성자 레퍼런스
