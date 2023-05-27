# 팩토리 패턴

## 심플 팩토리 패턴

### 정의

상위 객체를 추상화하고 하위 객체를 구체화하여 구현된 하위 객체를 상위 객체에 대입하여 사용하는 것.

### 특징

- 객체간의 느슨한 결합을 할 수 있다. → 여러 구현된 객체를 인터페이스 타입을 통해 유연하게 사용 가능

### 예시

```java

import org.junit.jupiter.api.Test;

public class test {

    interface School {
        public void grade();
    }

    static class ElementSchool implements School {
        @Override
        public void grade() {
            System.out.println(6);
        }
    }

    static class MiddleSchool implements School {
        @Override
        public void grade() {
            System.out.println(3);
        }
    }

    static class HighSchool implements School {
        @Override
        public void grade() {
            System.out.println(3);
        }
    }

    class SchoolFactory {

        public static School typeOfSchool(String type) {
            switch (type) {
                case "element" -> {
                    return new ElementSchool();
                }
                case "middle" -> {
                    return new MiddleSchool();
                }
                case "high" -> {
                    return new HighSchool();
                }
            }
            throw new IllegalArgumentException();
        }

    }

    @Test
    void test1() {
        School school = SchoolFactory.typeOfSchool("high");
        school.grade();
        school = SchoolFactory.typeOfSchool("element");
        school.grade();
        school = SchoolFactory.typeOfSchool("middle");
        school.grade();
    }
}
```

> 결과
> 

```java
3
6
3
```

## 추상 팩토리 패턴
