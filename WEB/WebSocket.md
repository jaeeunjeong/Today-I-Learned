# Web Socket
## 정의
HTTP 환경에서 TCP연결을 기반으로 실시간 양방향 통신을 가능하게 하는 프로토콜
## 동작 방식
1. 연결을 수립하기 위해 **Upgrade** 헤더와 **Connection**헤더를 포함하는 HTTP 요청을 보낸다.
## 웹소켓 구현
- TextWebsocketHandler나 BinaryWebSocketHandler를 상속받아 구현한다.
```
public class WebSocketHandlerImpl extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message);
        System.out.println(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished:" + session.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
```
- 구현한 웹 소켓 핸들러를 등록하는 config
```
@Configuration
@EnableWebSocket
public class PureWebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandlerImpl(), "/WebSocketHandlerImpl");
    }

    @Bean
    public WebSocketHandler webSocketHandlerImpl() {
        return new WebSocketHandlerImpl();
    }
}
```
