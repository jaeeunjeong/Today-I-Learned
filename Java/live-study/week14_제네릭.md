# 14주차 과제: 제네릭
> 자바의 제네릭에 대해 학습하세요.
## 학습 목록 
- [제네릭 사용법](#제네릭-사용법)
  - [제네릭 시 주의 사항](#제네릭-시-주의-사항)
- [제네릭 주요 개념 (바운디드 타입, 와일드 카드)](#제네릭-주요-개념-(바운디드-타입,-와일드-카드))
  - [와일드 카드](#와일드-카드)
  - [바운디드 타입](#바운디드-타입)
  - [제네릭의 형변환](#제네릭의-형변환)
- [제네릭 메소드 만들기](#제네릭-메소드-만들기)
- [Erasure](#Erasure)

## 제네릭 사용법
### 제네릭(generics) 이란
다양한 타입의 객체를 다루는 컬렉션 클래스나 메서드에서 형변환의 번거로움과 **타입의 안정성** 을 위해 사용할 데이터 타입을 명시해 주는 것.  
(컴파일 시에는 원시 타입으로 바뀌기에 런타임에러가 발생할 수 있는데 제네릭의 등장으로 컴파일 에러로 확인 할 수 있게됨.)
```
class Store<Bookstore>{}
```
### 제네릭 시 주의 사항
1. 한번에 하나의 제네릭만 사용 가능
```
class Store<Book, Fruit>(){} // X
```
2. 다른 형태로 사용은 상속의 관계에서는 가능 아니라면 불가.  

3. 제네릭 타입의 배열을 생성 할 수 없다.
- new 연산자는 생성 시점에 자료형을 분명하게 알아야하기 때문.
4. 제네릭 타입은 static멤버로 사용할 수 없다.
- static은 모든 객체에 대해 동일하게 동작해야 하기 때문.
#### 예제 
```
class Fruit			   { public String toString() {	return "FRUIT";	}}
class Apple extends Fruit {	public String toString() {	return "APPLE";	}}
class Grape extends Fruit {	public String toString() {	return "GRAPE";	}}
class Toy				  {	public String toString() {	return "TOY";	}}
class Box<E> {	
	ArrayList<E> list = new ArrayList<E>();
	void add(E item) {	list.add(item);	}
	E get(int index) {	return list.get(index);	}
	int size() {	return list.size();	}
	public String toString() {	return list.toString();	}
	
	//제네릭 타입의 배열을 생성할 수 없다. -> new 연산자 때문.
	//Error : Cannot create a generic array of E
	//E[] arr = new E[size()];

	//지네릭 멤버에 타입 변수 E는 static 사용 불가.
	//Error : Cannot make a static reference to the non-static type E
	//cf) 완전 다른 자료형이라면 아래와 같은 에러가 발생한다.
	//static E item; X cannot be resolved to a type
	//static E item;
}
class Main {
	public static void main(String args[]) throws IOException {

		Box<Fruit> fruitBox = new Box();//생성자에 지네릭을 명시하지 않았지만 에러는 발생하지 않음.
		//Error : Type safety: The expression of type Box needs unchecked conversion to conform to Box<Fruit>
		fruitBox.add(new Apple());
		fruitBox.add(new Grape());
		fruitBox.add(new Fruit());
		// fruitBox.add(new Toy()); //선언한 타입과 맞지 않기에 에러 발생.
		System.out.println("fruitBox.toString() : "+fruitBox.toString());
		
		//타입이 다르기에 에러 발생
		//Error : Type mismatch: cannot convert from Box<Grape> to Box<Apple>
		//Box<Apple> appleBox = new Box<Grape>();

		// JDK1.7 이후 부터는 제네릭 타입이 같다는 전제하에 생성자의 제네릭 타입 생략 가능. 
		Box<Apple> appleBox = new Box<>(); 
		appleBox.add(new Apple());
		appleBox.add(new Apple());
		
		//타입이 일치하지 않으면 에러 발생
		//Error : The method add(Apple) in the type Box<Apple> is not applicable for the arguments (Fruit)/(Grape)
		//appleBox.add(new Fruit());
		//appleBox.add(new Grape());
		System.out.println("appleBox.toString() : "+appleBox.toString());
  
	}
}
```
#### 실행 결과
```
fruitBox.toString() : [APPLE, GRAPE, FRUIT]
appleBox.toString() : [APPLE, APPLE]
```
## 제네릭 주요 개념 (바운디드 타입, 와일드 카드)

### 와일드 카드
> 제네릭은 아래 예시에서 확인 할 수 있듯, 오버로딩 관련하여 컴파일시 에러가 발생한다.  
여러 변수가 사용된다고 했을 떄, 하나에 한 자료형만 사용가능하기에 비효율적이며 확장성이 떨어지고 중복된 코드가 발생할 수 있다.
#### 예제
```
class Juicer {
	static String makeJuice(Box<Fruit> box) {
		StringBuffer sb = new StringBuffer();
		for (Fruit f : box.list)
			sb.append(f + " ");
		return sb.toString();
	}
	static String makeJuice(Box<Grape> box) {
	StringBuffer sb = new StringBuffer();
	for (Grape f : box.list)
		sb.append(f + " ");
	return sb.toString();
	}
}

class Main {
	public static void main(String args[]) throws IOException {
		Juicer juicer = new Juicer();
		Box<Apple> appleBox = new Box<>();
		appleBox.add(new Apple());
		appleBox.add(new Apple());
		//juicer.makeJuice(appleBox);//Juicer의 makeJuice에는 제네릭타입으로 Fruit만 선언되어있기에 에러 발생

		//상속의 관계에 있다면 정상 실행
		Box<Fruit> fruitBox =  new Box<Fruit>();
		fruitBox.add(new Apple());
		fruitBox.add(new Grape());
		fruitBox.add(new Fruit());
		System.out.println(juicer.makeJuice(fruitBox));
	}
}
```
#### 실행 결과
```
APPLE GRAPE FRUIT 
```
> **와일드카드**를 이용하면 더 범용성 좋게 활용할 수 있다.  
*하나의 참조변수로 서로 다른 타입이 대입된 여러 제네릭 객체를 다루기 위한 것*
#### 예제
```
class Juicer {
	static String makeJuice(Box<?> box) { // Box<Fruit> -> Box<?> 로 변경
		StringBuffer sb = new StringBuffer();
		for (Object f : box.list) //Fruit -> Object로 변경
			sb.append(f + " ");
		return sb.toString();
	}
}
class Main {
	public static void main(String args[]) throws IOException {
		Juicer juicer = new Juicer();

		Box<Apple> appleBox = new Box<>();
		appleBox.add(new Apple());
		appleBox.add(new Apple());

		//Juicer의 makeJuice에는 제네릭타입으로 Fruit만 선언되어있기에 에러 발생
		System.out.println(juicer.makeJuice(appleBox));

		Box<Fruit> fruitBox =  new Box<Fruit>();
		fruitBox.add(new Apple());
		fruitBox.add(new Grape());
		fruitBox.add(new Fruit());
		System.out.println(juicer.makeJuice(fruitBox));
	}
}
```
#### 실행 결과
```
APPLE APPLE 
APPLE GRAPE FRUIT 
```
### 바운디드 타입
> 와일드카드는 범위가 없기에 **super, extend**와 같은 걸로 범위를 제한한다.  
- extends : 와일드 카드의 상한 제한  
- super : 와일드카드의 하한 제한  
- ? : 범위가 없다.  

#### 예제
```
class Drink {
	static String makeDrink(Box<? extends Fruit> box) {
		StringBuffer sb = new StringBuffer();
		sb.append("ANY KIND OF JUICE YOU WANT : ");
		for (Object f : box.list)
			sb.append(f + " ");
		return sb.toString();
	}

	static String makeJuiceWithApple(Box<? super Apple> box) {
		StringBuffer sb = new StringBuffer();
		sb.append("JUICE WITH APPLE : ");
		for (Object f : box.list) sb.append(f + " ");
		return sb.toString();
	}
}

class Main {
	public static void main(String args[]) throws IOException {
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// StringTokenizer st;
		// st = new StringTokenizer(br.readLine());
		Drink drink = new Drink();

		Box<Fruit> fruitBox =  new Box<Fruit>();
		fruitBox.add(new Apple());
		fruitBox.add(new Grape());
		fruitBox.add(new Fruit());

		//제네릭타입이 Fruit이고 Grape는 자손이고 일단 형변환되면 상속 범위내에 있어서 에러가 발생하지 않는건가.
		System.out.println(drink.makeJuiceWithApple(fruitBox)); 
		System.out.println(drink.makeDrink(fruitBox));
		
		//타입 불일치로 컴파일 에러.
		Box<Grape> grapeBox =  new Box<Grape>();
		grapeBox.add(new Grape());
		//System.out.println(drink.makeJuiceWithApple(grapeBox));
		
		//상속의 경우
		Box<Apple> appleBox = new Box<>();
		appleBox.add(new Apple());
		appleBox.add(new Apple());
		System.out.println(drink.makeJuiceWithApple(appleBox)); //Box<? super Fruit> box로 바꾼다면 에러 발생.
		System.out.println(drink.makeDrink(appleBox));
	}
}
```
#### 실행 결과
```
JUICE WITH APPLE : APPLE GRAPE FRUIT 
ANY KIND OF JUICE YOU WANT : APPLE GRAPE FRUIT 
JUICE WITH APPLE : APPLE APPLE 
ANY KIND OF JUICE YOU WANT : APPLE APPLE 
```

### 제네렉의 형변환
일반적으로 불가능하지만 와일드카드를 이용하면 가능하다.
```
//Box<Fruit> appleBox =   (Box<Fruit>)new Box<Apple>();
Box<? extends Fruit> appleBox =  new Box<Apple>();
Box<? extends Fruit> appleBox2 = (Box<? extends Fruit>)new Box<Apple>();
```

## 제네릭 메소드 만들기
### 정의
메서드 선언부에 지네릭 타입이 선언된 메서드  
return type 앞에 선언됨.  
static 멤버에는 타입 매개변수 사용이 불가하지만, 매서드에 지네릭 타입을 선언하고 사용하는 것은 가능하다.  
매서드에 사용된 지네릭 타입은 지역 변수를 선언한 것과 같다.  
```
static <T> void sort(List<T> list,Comparator<? super T> c){}
```
**static \<T> void** : 같은 클래스 내에 있는 멤버들끼리는 참조 변수나 클래스 이름,  
즉, this나 클래스 이름을 생략하고 호출이 가능하지만,  
대입된 타입이 있을 때에는 반드시 써줘야 한다.  
```
~~<Fruit>sort()
~~.<Apple>sort()
```
메서드를 호출할 때 대입한 타입을 생략할 수 없다면, 참조변수나 클래스 이름을 반드시 붙여줘야한다.  
메서드를 호출 할 때마다 다른 지네릭 타입을 대입할 수 있게 한 것.
## Erasure
제네릭은 JDK1.5에 출시된 개념인데, **이전 버전과의 호환**을 위해 **컴파일 이후 제네릭타입이 제거**된다.  
### 제거 과정
> 제거 과정은 복잡하지만 간단하게 알아보자.
1. 제네릭 타입의 경계를 제거한다.  
제네릭 타입이 \<T extends Parent>라면 Parent,  
\<T> 라면 Object로 치환된다.
  이 후 클래스 옆의 선언은 제거된다.
#### BEFORE
```
class Box<E extends Fruit> {
	ArrayList<E> list = new ArrayList<E>();
}
```
#### AFTER
```
class Box { // <E> -> <Object>
	ArrayList<Fruit> list = new ArrayList<Fruit>();
```
2. 제네릭 타입을 제거한 후, 타입이 일치하지 않으면 형변환을 진행한다.
와일드카드가 포함되어 있다면 적절한 타입으로 치환된다.*(최상위 타입이지 않을까...)*

 ### 참고 자료  
  남궁 성, Java의 정석, 도우출판  
