# Web Socket
## 정의
HTTP 환경에서 TCP연결을 기반으로 실시간 양방향 통신을 가능하게 하는 프로토콜
## 개요
1. 일반적으로 서버와 클라이언트의 통신은 HTTP를 이용해서 됨.
2. HTTP 프로토콜은 요청에 대한 응답만 진행하고 연결의 수립과 끊은 과정을 반복하기에 계속해서 메시지를 주고 받는다면 같은 행위를 반복해야하는 비효울적인 문제가 있었음.
3. 웹소켓 프로토콜을 통해 실시간 양방향 통신이 가능해짐
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
