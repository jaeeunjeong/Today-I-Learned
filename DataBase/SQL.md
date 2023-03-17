# SQL
## SQL
Structure Query Language  
데이터를 질의하기위해 사용하는 언어  
SQL을 이용하여 데이터를 제어하고 관리 조작하는 언어

## 종류
### DDL
Data Definition Language  
데이터 테이블의 구조를 관리하기 위한 언어  
테이블을 생성하거나 구조 변경, 제거, 이름 변경 등의 구조 관련 명령어
- CREATE : 테이블 생성
- ALTER : 테이블 구조 변경
- DROP : 테이블 제거
- RENAME : 테이블 이름 재정의

### DML
Data Manipulation Language
데이터를 조작하기 위한 언어.  
원하는 데이터를 조회하거나 데이터의 삽입, 수정, 삭제 등의 데이터 처리에 사용하는 언어  
- INSERT : 데이터 추가
- SELECT : 데이터 검색(조회)
- UPDATE : 데이터 수정
- DELETE : 데이터 삭제

### DCL
Data Control Language  
데이터베이스에 접근하여 데이터를 사용할 수 있도록 권한을 부여하거나 회수하는 명령어
- GRANT : 사용자에게 권한 부여
- REVORK : 사용자에게 부여된 권한 회수

### TCL
Transaction Control Language  
트랜잭션 제어하는 언어  
논리적 작업 단위로 DML에 의해 작업 된 결과를 트랜잭션 단위로 제어하는 명령어  
- COMMIT : 트랜잭션의 작업 결과를 반영
- ROLLBACK : 트랜잭션 작업 취소 및 이전 작업으로 되돌리기

## 명령어 
### case-when
- 다중 조건문을 쓸 때 사용
```
case
  when '조건' then '결과 값'
  else '결과 값'
end
```
sum으로도 사용 가능!
```
case a.order_status
 when 'reservation' then 1
 else 0 end reservation_count
```
조건에 맞으면 reservation_count에 1씩 추가된다.
### 번호 매기기
```
select CAST(@ROWNUM := @ROWNUM + 1 ) AS 'No',
```
- := 0 : 0 으로 초기화

### LIKE vs RLIKE
#### LIKE
- **%** : 여러 개의 글자와 매칭된다.
- **_** : **_** 하나 당 한 글자와 매칭된다.
#### RLIKE
REGEXR LIKE!  
정규식을 사용할 수 있는 LIKE!  
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

### DDL
- USE **DB 이름** : DB에 들어가기 위한 명령어
- GRANT ALL PRIVILEGES ON userid.* TO 'root'@'아이피' : 권한 주는 명령어
- USE mysql에서 유용하게 쓰이는 명령어
  - select user, host from user; : 어떤 계정이 어떤 환경에 권한이 있는지 알 수 있다.  

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
