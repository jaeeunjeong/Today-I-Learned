# MessageQueue

## 정의
프로세스 간의 데이터 교환을 위해 사용하는 통신 방법의 하나로, 메시지를 받아 전송하는 시스템을 제공한다.
## 구조
1. Publisher에서 메시지를 전송
2. Message Queue 에서 메시지를 수신받음
3. Subscriber로 메시지를 발송
   1. 메시지가 정상 발송되지 않으면 Dead-Letter Queue에서 재처리
## 종류
- RabbitMQ
  - 메시지 브로커 
- Kafka
  - 이벤트 스트리밍 플랫폼
- SQS 
  - Amazon에서 제공하는 Queue 서비스 
