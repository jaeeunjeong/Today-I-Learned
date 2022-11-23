## 정의
REST API 문서를 쉽게 문서화 할 수 있도록 도와주는 툴

## 설정
- gradle
```
compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.5.0'
compile group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.5.0'
```
- 빈으로 swagger 설정을 등록
```
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
                .build();
    }
}
```


*getter로 응답 값 설정이 되는 거 같은데 찾아봐야함*
*@apioperation 이용하는데, responseClass와 responseClass에 getter를 설정해주었을 때 값이 나왔었음.*
