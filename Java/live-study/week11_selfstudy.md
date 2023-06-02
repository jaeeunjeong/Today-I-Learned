# enum 활용기
## enum 값과 관련된 로직은 enum 내에서 해결해보기
- 비용 객체내에서 값들을 관리하기 때문에 통일성을 갖출 수 있음
- 싱글톤 방식을 보장한다.
```java
public enum PaymentFee {

    MINIMUM_FREE_FEE(50000L),
    DELIVERY_FEE(2500L);

    private Long money;

    private PaymentFee(Long money) {
        this.money = money;
    }

    public static Long checkDeliveryFee(Long payment) {
        return payment < PaymentFee.MINIMUM_FREE_FEE.getMoney() ?
            payment + PaymentFee.DELIVERY_FEE.getMoney() : payment;
    }
}
```
