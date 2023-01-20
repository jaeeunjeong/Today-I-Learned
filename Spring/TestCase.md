# 테스트 케이스 코드
- 테스트 케이스르 만들어서 작업이 제대로 수행되는 지 확인 하는 것
- 정상적으로 작동하는지 확인 할 수 있어서 미리 예외를 확인할 수 있다.
- 테스트케이스를 작성하다보면 작업의 단위를 최소화하기때문에 작업들 간의 결합도르 낮추 수 있다.
## 예제 : 비밀번호 검증하기
###  비밀번호 검증 로직
```
public class PasswordValidator {
    public static void validate(String password) {
        if (password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("비밀 번호는 최소 8자 이상 12자 이하여야합니다.");
        }
    }
}
```
### 비밀번호 테스트 코드
```
import example.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class PasswordValidatorTest {

    @DisplayName("비밀번호는 8자 이상 12자 이하이다.")
    @Test
    void test1() {
        assertThatCode(() -> PasswordValidator.validate("test123467"))
                .doesNotThrowAnyException();
    }
    
    @DisplayName("비밀번호는 8자 이하 12자 이상인 경우 예외 발생")
    @ParameterizedTest // 파라미터를 넣어줄 수 있다.
    @ValueSource(strings = {"abbccd", "abbbcccddssser"})
    void test2(String password) {
        assertThatCode(() -> PasswordValidator.validate(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀 번호는 최소 8자 이상 12자 이하여야합니다.");
    }
}
```
