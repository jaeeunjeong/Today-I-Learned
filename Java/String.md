# String
- 문자열
- char 형태의 배열
## 선언 방법
```java
String str1 = new String("apple");
String str2 = "apple";
String str3 = "apple";

System.out.println(String.format("str1 == str2 %s", (str1 == str2)));
System.out.println(String.format("str2 == str3 %s", (str2 == str3)));
System.out.println(String.format("(str1).equals(str2) %s", (str1).equals(str2)));

```
| 결과
```
str1 == str2 false
str2 == str3 true
(str1).equals(str2) true
```
- new 를 이용하면 heap에 생성되고, ""를 이용해서 생성하면 heap안에 있는 string pool에 생성된다고 한다.
