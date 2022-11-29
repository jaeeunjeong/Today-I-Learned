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

### case-when
- 다중 조건문을 쓸 때 사용
```
case
  when '조건' then '결과 값'
  else '결과 값'
end
```
