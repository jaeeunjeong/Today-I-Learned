#	SOP(Same-origin policy)
같은 출처에서만 리소스를 공유할 수 있다라는 규칙을 통해 다른 출처에서 가져온 리소스와의 상호작용을 제한하는 보안 방식 가진 정책.

# CORS (Cross-Origin Resource Sharing, CORS)
## 정의
웹 애플리케이션은 자신의 출처가 다른 곳에 접근하고자 할 때 보안상의 이유로 접근을 제한되기에,(SOP)   
다른 출처에 접근하여 리소스를 불러오려면 접근을 허가한다는 응답을 받아야함.  
## 접근 제어를 위한 요청 시나리오
## Simple requests
Method, content-Type, header의 값이 모두 존재하고 아래 조건을 충족하는 요청을 의미
- **Method** : GET, HEAD, POST 중 하나.
- **Content-Type**가 아래 중 하나
+ application/x-www-form-urlencoded
+ multipart/form-data
+ text/plain
 - **User Agent**가 자동으로 설정한 헤더
  + Accept
  + Accept-Language
  + Content-Language
  + Content-Type

서버 헤더의 **Access-Control-Allow-Origin**에 요청의 Origin 헤더에서 전송된 값을 설정하여 맞으면 접속을 허가한다.  
### 동작 방식
1. 클라이언트(브라우저)는 서버에 접속을 요청하고 서버는 응답을 확인한다.  

### 요청 헤더
```
GET /resources/public-data/ HTTP/1.1
Host: iamserver.real
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:71.0) Gecko/20100101 Firefox/71.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-us,en;q=0.5
Accept-Encoding: gzip,deflate
Connection: keep-alive
Origin: https://hello.client
```
헤더의 Origin에서 요청이 https://hello.client 임을 알 수 있다.  

2. 서버는 **Access-Control-Allow-Origin**에 클라이언트의 정보를 넣어 접근을 허거한다.
```
HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 00:23:53 GMT
Server: Apache/2
Access-Control-Allow-Origin: https://hello.client
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
Transfer-Encoding: chunked
Content-Type: application/xml

[…XML Data…]
```
## Preflight request
- 헤더에 **OPTIONS**메서드를 추가하여 다른 도메인의 리소스로 미리 요청을 보내 이 요청이 안전한지 확인.
- CORS가 생기기전, SOP request만 가능하다는 가정하에 서버가 만들어졌는데, CORS가 생긴 후 cross-site request가 가능해지면서 보안문제가 발생하게 되면서 등장.

### 동작 방식
1.	브라우저에 다른 출처의 접속하여 응답을 받아오려할 때, 먼저 안전한 요청인지 확인하는 preflight를 보냄.
2.	서버는 허용 및 금지하고 있는 정보(Access-Control-Request-Method, )를 응답.
3.	브라우저는 클라이언트의 정보와 서버로부터 응답받은 정보를 비교하여 원하는 정보를 요청.
4.	서버는 요청에 응답을 통해 동작 종료.

### Spring에서 CORS 해결 방법 
-	@CrossOrigin을 이용하여 해당하는 메서드나 클래스에 개별 적용.
-	WebMvcConfigure를 implements받아서 addCorsMappings 에 알맞게 작성하여 해결.
- HandlerInterceptor를 implements받아서 retrieve에 알맞게 작성하여 해결.
- CorsFilter를 extends하여 CorsConfiguration자료형을 이용하여 알맞게 작성하여 해결.


### 참고
실제 POST 요청에는 `Access-Control-Request-*` 헤더가 포함되지 않는다.  
최초의 OPTIONS 요청에만 필요하다.  


#### 참고 문헌
https://developer.mozilla.org/ko/docs/Web/HTTP/CORS
