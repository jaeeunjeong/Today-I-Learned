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


## 식별관계/비식별관계
### 식별관계
- 부모테이블의 기본키나 유니크키를 자식테이블의 기본키로 사용하는 경우
- 부모 테이블에 키가 있어야 자식 테이블에서도 생성 가능하다.
- 데이터 정합성을 DB에서도 체크해준다.
### 비식별관계
- 부모테이블의 기본키나 유니크 키를 외래 키로 사용하는 관계
- 요구 사항이 변할 경우 유연하게 대처할 수 있다.
- 자식 데이터가 존재해도 부모데이터가 존재하지 않을 수 있다.
- 데이터 무결성을 보장하지 않는다.
# GroupBy 가 안 될 때가 있다.
- 테이블 전체를 사용해야 group by가 되는 경우가 있는데 이런 경우 use mysql을 한 후  
   **SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));** 를 해주면 된다.
```
[42000][1055] Expression #15 of SELECT list is not in GROUP BY clause and contains nonaggregated column 'cloudgatedb.chatroom0_.id' which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by
→ SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```
## Replication
### 정의
DataBase를 두 개로 나뉘어서 저장하는 것으로 master/slave로 나누어서 쓰기 전용/ 읽기 전용으로 사용하여 DB에 접속 부하를 줄이기 위해 사용한다.
### 특징
- 바이너리 로그 기반으로 이뤄짐
- 비동기방식
- select 성능 향상
- 데이터 백업 기능
- 데이터 정합성을 보장하지 않음
