# 1주차 항목 관련 추가 학습
> 자바의 call by value/ call by reference에 대해 학습합니다.

## 학습 목록
- [call by value/ call by reference](#call-by-value/-call-by-reference)

## call by value/ call by reference
### call by value
- 객체를 값의 형태로 저장하는 것
- 객체를 변경하면 객체의 값이 변경된다.
원시형의 경우 call by value 라고 할 수 있다.  
### call by reference
메모리 내에서 값이 있는 공간의 주소를 호출 하는 것.  
값이 있는 공간의 주소를 복사한다.  
### 자바는 call by value!(단, 물리적 관점)
자바는 객체가 생성되면 메모리 공간을 만든다.(Heap Area)  
해당 메모리 공간은 주소를 가지고 있는데 이 주소는 Heap 영역에 저장된다.  
자바는 객체의 주소 정보를 호출하고 이 주소 정보를 이용하여 메모리의 값을 확인할 수 있다.  
주소 값을 리턴하기때문에 call by value가 되는 것!  
(객체가 반환되면 그 객체는 Garbage Collector에 들어가게 된다.)
- 자바에서 reference 타입 객체는 heap에 생성되지만, stack에서 그 객체의 주소값을 사용하기 때문에 직접 객체에 접근하는 것이 아니라 메모리를 확인하기 때문에 reference 타입 객체를 변경한다고 하여 직접 값이 변경되는 것이 아니라 call by value이며 값의 변경을 원한다면 그 객체들을 직접 바꿔줘야한다. 
