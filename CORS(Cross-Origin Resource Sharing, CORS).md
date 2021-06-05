# CORS (Cross-Origin Resource Sharing, CORS)
## 정의
A라는 애플리케이션에 B라는 애플리케이션이 접근하려고 할 때, 보안상의 이유로 접근이 불가능하다.  
이때, 접근이 가능하도록 권한을 부여하는 것을 의미한다.
## 원인
보안 상의 이유로 도메인(출처)이 다르다면 리소스를 불러올 수 없고, 동일한 것만 가능하다.  
만약, 다른 도메인간의 리소스를 불러오려면 올바른 CORS 헤더를 포함한 응답을 반환해야한다. 

## 사용하는 경우
 - XMLHttpRequest와 Fetch API 호출.
 - 웹 폰트(CSS 내 @font-face에서 교차 도메인 폰트 사용 시)
 - drawImage() (en-US)를 사용해 캔버스에 그린 이미지/비디오 프레임.
 - 이미지로부터 추출하는 CSS Shapes.

## 기능적 개요
웹 브라우저에서 해당 정보를 읽는 것이 허용된 주소를 서버에서 알 수 있는 새로운 HTTP 헤더를 추가함으로써 동작한다.  
추가적으로, 서버 데이터에 부수 효과(side effect)를 일으킬수 있는 HTTP 요청 메서드에 대해 CORS에는 다음과 같이 명세되어있다.  
1. 브라우저가 요청을 OPTION 메서드로 preflight하여 지원하는 메서드를 요청한다.  
0. 이후 서버의 허가가 떨어지면 실제 요청을 보내도록 요구한다  

추가로 서버는 클라이언트에 "인증 정보"를 요청과 함께 보내야한다고 알려줄 수도 있다.  

## Simple requests
`하나의 메서드 + User Agent가 자동으로 설정한 헤더` 를 갖고 요청
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
1. OPTIONS 메서드를 통해 다른 도메인의 리소스로 HTTP 요청을 보낸 후, 실제 요청이 전송하기에 안전한지 확인한다.
0. 안전하다면 다시 요청하고 응답받고, 안전하지 않다면 재요청하지 않는다.  

### 참고
실제 POST 요청에는 `Access-Control-Request-*` 헤더가 포함되지 않는다.  
최초의 OPTIONS 요청에만 필요하다.  


#### 참고 문헌
https://developer.mozilla.org/ko/docs/Web/HTTP/CORS
