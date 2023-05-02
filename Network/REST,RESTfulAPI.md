# REST/ REST API 
## 목차
- [REST](#REST)
- [REST API](#REST-API)

## REST
REpresentational State Transfer
### 정의
HTTP URI 를 통해 자원을 명시하고,  
HTTP Method를 통해 해당 자원에 대한 CRUD Operation을 적용하는 것.
*자원 이란, 다양한 애플리케이션이 클라이언트에게 제공하는 정보*
#### 사용하는 이유
- 다양한 언어나 프레임워크 등 멀티 플랫폼의 지원 가능  
- 대규모의 고성능 통신을 안정적으로 지원할 수 있다.
### 원칙
1. client-server : 클라이언트 서버 구조로 되어있어 각각에 의존하지 않아야한다.
2. cache : 캐시 성질을 명시해줌으로써 확인되어야함
3. stateless : 요청간에는 데이터의 상태가 되어있어야하고, 이전 요청 및 다음 요청에 대해 알 수 없어야한다.
4. uniform interface : 규칙을 URI에 분명하게 나타내어 의미를 파악할 수 있어야한다.
5. layered system : 계층적으로 되어 있어 클라이언트는 서버에 어디에 접속된건지 알 수 없다.
6. cade on demand(optional)
### 구성요소
1. HTTP URI : 자원
2. HTTP Method : 자원에 대한 행위
3. HTTP Message Pay Load :자원에 대한 행위의 내용
### 특징
- HTTP 매커니즘을 따른다.
- statless : 세션이 필요가 없고 각 요청에서 처리에 필요한 모든 정보가 필요하고, 서버는 클라이언트 정보를 저장할 수 없다.
- Server/Client 구조
- 캐시 가능
- 균일한 인터페이스
### 장점
- 유연성 : HTTP 프로토콜을 따르는 모든 플랫폼에서 사용가능하다.
### 단점
- 자원에 대한 행위가 한정되어 있다.
## REST API
REST 아키텍쳐 스타일을 따르는 API
### 작동 원리
1. 클라이언트가 서버에 특정 URI를 기반으로 원하는 요청을 전달합니다.
2. 클라이언트의 요청을 바탕으로 권한을 확인하고, 내부적으로 처리합니다.
3. 서버가 클라이언트에 정보를 반환합니다.
