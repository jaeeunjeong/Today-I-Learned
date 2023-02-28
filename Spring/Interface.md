## HandlerMethodArgumentResolver
### 정의
- Strategy interface for resolving method parameters into argument values in the context of a given request.  
- 주어진 요청을 처리할 때, 메소드 파라미터를 인자값들에 주입 해주는 전략 인터페이스.
### 
```
boolean supportsParameter(MethodParameter parameter);
```
핸들러의 특정 파라미터를 지원 여부를 판단하기 위한 메서드
```
@Nullable
Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception;
```
해당 파라미터에 대해 실질적인 로직을 처리하는 곳 
데이터를 변환해준다.  
```
@Override
public boolean supportsParameter(MethodParameter parameter) {
    return isAssignable(IndexingMessage.class, parameter.getParameterType());
}
```
```
@Slf4j
@AllArgsConstructor
public class CustomContentResolver implements HandlerMethodArgumentResolver {
    private final MessageConverter messageConverter;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isAssignable(Content.class, parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        Object convertMessage = messageConverter.fromMessage(message, Content.class);
        return ((NotificationRequestConverter.NotificationRequest) convertMessage).getMessage();
    }
```
cf)
```
isAssignable
```
