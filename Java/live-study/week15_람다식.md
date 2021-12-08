# 15주차 과제: 람다식
> 자바의 람다식에 대해 학습하세요.

## 학습 목록 
- 람다식 사용법
 - 함수형 인터페이스
 - Vriable Capture
 - 메소드, 생성자 레퍼런스

## 람다식 사용법
### 람다식란?
메서드를 화살표를 이용하여 하나의 식으로 간략하게 나타낸 것을 의미한다.  
람다식은 실제로는 익명 객체이다.  
#### BEFORE
```
void sum(int a, int b){
    return a+b;
}
```
#### AFTER
```
(int a, int b) -> return a+b; 
```
#### 변환 방법
1. 메서드의 이름과 반환타입을 제거한다. 
2. 매개변수 선언부와 메서드 본체의 대괄호 {} 사이에 화살표를 추가힌디.  
또한 람다식에 선언된 매개변수가 충분히 추론 가능하거나 return 타입이라면 생략도 가능하다.  
### 주의 사항
- 괄호 {} 안이나 return 이 아니라면 끝에 ;를 붙이지 않는다. *이 경우는 식이기 떄문.*
- 매개 변수가 하나라면 괄호를 생략할 수 있다.
- 괄호 {} 안에 return 타입이라면 괄호 {}의 생략도 가능하다.
## 함수형 인터페이스
람다식은 이름을 제거하기 떄문에 익명 클래스의 객체라고도 할 수 있다.  
람다식를 호출하여 사용하기 위해선 참조변수가 필요하다.  
하지만 우리는 어떤 데이터 타입을 사용해야할 지를 모른다.  
*따라서,* 모든 데이터타입의 조상인 **Object**에 저장해보았다.  
이렇게 Object에 선언하면 일단 에러는 발생하지 않지만 위의 주석과 같은 경고가 발생하며, **obj**를 이용하여 람다식 안의 **printOne**를 사용하려고 해도 사용할 수 없다.  

```
//The value of the local variable obj is not used
Object obj = new Object() {
	void printOne() {
		System.out.println(1);
	}
};
```
람다식에서는 함수형 인터페이스를 참조변수 타입으로 사용한다.  
함수형 인터페이스 즉, 인터페이스 형태가 람다식의 참조변수로서 사용가능한 이유는 람다식의 매게변수의 타입과 개수, 리턴타입이 인터페이스를 구현한 익명객체가 일차하기 떄문이다.
#### 예제
```
public class Main {

	public static void main(String[] args) throws IOException {

		// The value of the local variable obj is not used
		Object obj = new Object() {
			void printOne() {
				System.out.println(1);
			}
		};
		MyFunction myFunction = new MyFunction() {
			public void printOne() {
				System.out.println(1);
			}
		};
		myFunction.printOne();

	}
}

@FunctionalInterface
interface MyFunction {
	public abstract void printOne();
}
```
#### 결과
```
1
```
### 주의 사항
1. 함수형 인터페이스에는 오직 하나의 추상 메서드만 정의되어야 한다.  
그래야 람다식과 인터페이스 메서드가 1:1로 연결될 수 있다.  
0. @FunctionalInterface를 꼭 붙여주자.  
-> 컴파일러가 함수형 인터페이스를 올바르게 구현하였는지 확인해준다.
### 함수형 인터페이스 타입의 매개변수와 반환타입
추상 메서드가 선언된 인터페이스를 매개변수로서 사용한다면, 이 메서드를 호출할 떄 람다식을 참조하는 매개변수를 지정해주어야한다.  
따라서 람다식을 참조변수처럼도 사용가능한데, 이는 메서드를 이용해서 람다식을 주고 받을 수 있다는 뜻이다.  
즉, 변수처럼 메서드를 다룰 수 있음을 의미한다.  
참조변수 없이 직접 람다식을 매개변수로 지정할 수 있다.  
메서드의 반환 타입이 함수형 인터페이스 타입이라면 이 함수형 인터페이스의 메서드와 동등한 람다식을 가리키는 참조변수를 반환하거나 람다식을 직접 반환할 수 있다.  

```
import java.util.*;
@FunctionalInterface
interface DrawingInterface{
	public abstract void sketch();
}
class Practice {
	//매개변수 타입이 DrawingInterface인 메서드.
	static void execute(DrawingInterface drawingInterface) {
		drawingInterface.sketch();
	}
	
	static DrawingInterface getDrawingInterface() {
		DrawingInterface d = () -> System.out.println("LET'S SKETCH!!");
		return d;
	}
	
	public static void main(String[] args) {
		// 함수형 인터페이스를 이용하여 람다식을 사용
		DrawingInterface d1 = () -> System.out.println("WARMING UP!");
		
		// 익명클래스를 이용하여 람다식을 구현
		DrawingInterface d2 = new DrawingInterface() {
			@Override
			public void sketch() {//public을 반드시 붙여야 함.
				System.out.println("SETTING");
			}
		};
		
		//반환 타입이 DrawingInterface인 메서드 구현.
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
