# CORS (Cross-Origin Resource Sharing, CORS)
## 정의
Cross Origin Resource Sharing 의 약자로 
웹 애플리케이션은 자신의 출처(도메인, 프로토콜, 포트 등)과 다른 곳에 접근하고자 할때 보안상의 이유로 접근을 제한한다.  
다른 출처에 접근하여 리소스를 불러오려면 접근을 허가한다는 응답을 받아야한다. 
## 원인
보안 상의 이유로 도메인(출처)이 다르다면 리소스를 불러올 수 없고, 동일한 것만 가능하다.  
만약, 다른 도메인간의 리소스를 불러오려면 올바른 CORS 헤더를 포함한 응답을 반환해야한다. 

## Simple requests
서버 헤더의 **Access-Control-Allow-Origin**에 요청의 Origin 헤더에서 전송된 값을 설정하여 맞으면 접속을 허가한다.
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
#### 하나의 메서드
GET, HEAD, POST
#### User Agent가 자동으로 설정한 헤더
- Accept
- Accept-Language
- Content-Language
- Content-Type (아래의 추가 요구 사항에 유의하세요.)
- DPR
- Downlink
- Save-Data
- Viewport-Width
- Width
##### Content-Type 헤더는 다음의 값들만 허용됩니다.
 - application/x-www-form-urlencoded
 - multipart/form-data
 - text/plain
## Preflight
- simple request와 달리 **OPTIONS**메서드를 통해 실제 요청이 전송하기에 안전한지 확인한다.
- Cross-site 요청은 유저 데이터에 영향을 줄 수 있기에 미리 전송해야한다.

### 참고
실제 POST 요청에는 `Access-Control-Request-*` 헤더가 포함되지 않는다.  
최초의 OPTIONS 요청에만 필요하다.  


#### 참고 문헌
https://developer.mozilla.org/ko/docs/Web/HTTP/CORS
