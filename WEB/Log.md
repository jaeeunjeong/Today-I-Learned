# Log
- 정의 : 서버에서 발생한 이벤트들을 기록하는 것
## ForMat
```

```
- 날짜와 시간
- 로그 레벨
- 프로세스 ID
- 구분자
- 스레드 이름
- 로거 이름
- 로그 메시지
## File Output
**application.properties**안에 **logging.file.name**이나 **logging.file.path**프로 퍼티를 설정해야한다.
## Logback
 Java 기반 Logging 라이브러리
## 특징
1. application 가동시 classpath내의 환경 설정 파일을 검색해서 logback을 초기화시킨다.
2. springboot에서는 logback-spring.xml을 읽어서 spring의 logback 구동을 지원.
3. spring.profiles.include 를 이용하면 설정을 플러그인처럼 사용가능.
4. springProfile을 이용하여 프로퍼티의 환경변수와 동일하게 설정을 진행할 수 있다.
