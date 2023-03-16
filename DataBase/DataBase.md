
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
## GroupBy 가 안 될 때가 있다.
- 테이블 전체를 사용해야 group by가 되는 경우가 있는데 이런 경우 use mysql을 한 후  
   **SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));** 를 해주면 된다.
```
[42000][1055] Expression #15 of SELECT list is not in GROUP BY clause and contains nonaggregated column 'cloudgatedb.chatroom0_.id' which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by
→ SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```
