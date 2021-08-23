# Trigger

- 일종의 프로시저
- 테이블의 특정 이벤트에 반응하여 DB상에서 실행되는 작업을 의미한다.
- event 종류 : INSERT, UPDATE, DELETE

## Trigger의 종류
- 행 트리거
테이블 안의 관련있는 행들에 대해 각각 진행
변경 전 또는 변경 후의 레코드는 OLD, NEW라는 가상의 줄변수를 이용해서 사용 가능.

- 문장 트리거 
INSERT, UPDATE, DELETE에 대해 한 번만 실행된다.

## OLD/NEW 키워드
- OLD : 이벤트가 일어나기 전 데이터
- NEW : 이벤트가 일어난 후 데이터
## 이벤트에 따라 사용할 수 있는 키워드가 정해져 있음.
- INSERT : NEW (추가전 OLD데이터가 없기 때문에 NEW만 사용 가능)
- UPDATE : OLD / NEW
- DELETE : OLD

## 사용
OLD.컬럼명, NEW.컬럼명

## 생성 방법
```
DELIMITER //
CREATE TRIGGER <트리거 이름> <활성화시간> <이벤트> ON <테이블명>
    FOR EACH ROW
    BEGIN
    실행문 ;
    ....
    UPDATE IN_STOCK SET ITEM_COUNT = CUR_ITEM_COUNT WHERE ITEN_PURCHASE_ID = NEW.ITEN_PURCHASE_ID;
    INSERT INTO HISTORY_ITEM_PURCHASE (ITEN_PURCHASE_ID, REG_DATE);
    ....
    END
DELIMITER ;

```

## 트리거 확인
```
SHOW TRIGGERS;
```

