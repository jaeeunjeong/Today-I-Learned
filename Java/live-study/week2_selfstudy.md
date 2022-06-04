# 2주차 항목 관련 추가 학습
> 자바의 Wrapper CLass에 대해 공부합니다.

## 학습 목록
- [Wrapper Class](#Wrapper-Class)

## Wrapper Class
기본형을 객체로 감싸서 객체형으로 사용하게끔하는 것
### 등장 이유
매개변수로 객체가 요구 되거나, 기본형 값이 아닌 객체로 저장해야 할 때, 객체간의 비교가 필요할 때가 있다.  
~~### 매개변수로 객체가 요구되는 예시~~

~~### 기본형이 아닌 객체로 저장되는 예시~~

~~### 객체간의 비교가 필요한 예시~~

### Boxing/UnBoxing
Wrapper Class와 Primitive Type으로 필요한 상황에 따라 자료형을 바꾸는 경우를 말한다.
Wrapper -> Primitive : UnBoxing
Primitive -> Wrapper : Boxing
|   |Wrapper|Primitive|
|---|---|---|
|Null 가능 |  O  |  X  |
|연산 가능 |  X | O |
**JDK1.5부터는 AutoBoxing/AutoUnboxing이 도입되어 지원되었다.**
~~하지만 권장하지 않는 게 속도가 느리다.~~
### 형태
|Wrapper|Primitive|
|---|---|
|**Integer** |int|
|**Character** | char  |
|Byte|byte|
|Short|short|
|Long|long|
|Float|float|
|Double|double|
|Boolean|boolean|
