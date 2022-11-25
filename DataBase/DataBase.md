## LIKE vs RLIKE
### RLIKE
REGEXR LIKE!  
정규식을 사용할 수 있는 LIKE!  

### LIKE vs RLIKE
#### LIKE
- **%** : 여러 개의 글자와 매칭된다.
- **_** : **_** 하나 당 한 글자와 매칭된다.
#### RLIKE
- **$**  :  패턴 매칭에서 끝나는 문자
- **^**  :  패턴 매칭에서 시작하는 문자
- **.**  :  **.** 하나 당 한 글자와 매칭된다.
- **"*"**  :  여러개의 단어와 매칭 된다.
```
SELECT name, price FROM STORE WHERE product_type = '과일'  price RLIKE CONCAT(900,'$');
+------------++------------+
| fruit_type | price      |
+------------++------------+
| grapes     | 6900       |
| bluebarry  | 5900       |
| apple      | 2900       |
| plum       | 12900      |
| watermelon | 22900      |
| banana     | 3900       |
| mango      | 9900       |
+------------++------------+
```

## 집합 연산자 
DB에서 결과를 조합하여 사용할 때 FROM에서 테이블을 조합하여 사용할 수도 있지만 SELECT에서 결과를 조합하여 사용할 수 있다.
이때 사용하는 연산자를 집합 연산자라고 한다.
### 종류
- UNION 합집합 **(A∪B - A∩B)**
```
SELECT user_id, user_name
FROM student
WHERE user_age > 10;
UNION
SELECT user_id, user_name
FROM student
WHERE user_age < 5;
```
- UNION ALL : 합집합이되 distinct아님 **A∪B**
```
SELECT user_id, user_name
FROM student
WHERE user_age > 6;
UNION
SELECT user_id, user_name
FROM student
WHERE user_age < 8;
```
-> 중복되는 user_age가 7일때의 값이 두번 출력된다.
- INTERSECT : 여러 개의 SQL 문의 결과에 대한 교집합. -> IN, EXISTS로도 사용해도 같은 쿼리가 나온다. **A∩B**
```
SELECT user_id, user_name
FROM student
WHERE user_age > 6;
INTERSECT
SELECT user_id, user_name
FROM student
WHERE user_age < 8;
```
-> 중복되는 user_age가 7만 출력된다.
- EXCEPT : 차집합, 앞의 결과에 대해 뒤의 결과가 중복되면 그 값은 제거되어 결과를 나타낸다.-> NOT IN NOT EXISTS로도 변경 가능**A-B**
```
SELECT user_id, user_name
FROM student
WHERE user_age > 6;
EXCEPT
SELECT user_id, user_name
FROM student
WHERE user_age < 8;
```
-> user_age가 6보다 큰 경우에 뒤의 쿼리와 중복으로 겹쳐지는 7보다 큰 경우는 제외한 채 6보다 큰 결과가 출력.

## 식별관계 vs 비식별관계
- 식별관계 : 부모테이블의 고유 값이 자식 테이블의 기본키로 되어 있는 관계
- 비식별관계 : 부모 테이블의 고유값(기본키, 유니크키) 가 자식 테이블의 기본키가 아닌 관계
