## 전화번호 -(하이픈) 추가 및 형태 만들어주는 정규식
**class = "phoneNumber"** 로 동작한다.
```
$(document).on("keyup", ".phoneNumber", function() { $(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") ); });
```
