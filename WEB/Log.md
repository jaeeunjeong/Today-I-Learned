# Log
- 정의 : 서버에서 발생한 이벤트들을 기록하는 것
## Logback
 Java 기반 Logging 라이브러리
## 특징
1. application 가동시 classpath내의 환경 설정 파일을 검색해서 logback을 초기화시킨다.
2. springboot에서는 logback-spring.xml을 읽어서 spring의 logback 구동을 지원.
3. spring.profiles.include 를 이용하면 설정을 플러그인처럼 사용가능.
