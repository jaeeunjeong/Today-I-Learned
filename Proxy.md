# Proxy
클라이언트 - 서버 사이에 위차하여 "프록시"를 이용하여 다른 네트워크 서비스에 접근을 통해 간접적으로 서버를 이용할 수 있게 해주는 컴퓨터 시스템이나 응용프로그램을 의미.

## Forward Proxy
클라이언트의 요청을 받아서 인터넷에 연결한 후 결과를 클라이언트에 전달한다.
cache 등을 이용하게 하는 방법. 
요청 받는 서버는 Forward Proxy server에서 요청을 받기때문에 클라이언트에 대한 정보를 알 수 없다.
burpsuite가 아웃바운드로 나가는 걸 잡음.
### burpsuite


## Reverse Proxy
클라이언트가 데이터를 요청하면 리버스 프록시가 클라이언트의 요청을 받아서 내부 서버에 데이터를 전달하고 전달받은 후, 클라이언트에게 전달한다.  
클라이언트는 내부의 서버에 대해 알 필요가 없고, 리버스 프록시 서버에 요청하여 통신한다.  
내부 서버에 대한 설정으로 로드벨런싱이 서버 확장에 유리하다.  
apache나 nginx가 inbound로 들어오는 걸 잡아서 각 was로 분배, ssl, 필터링 등을 수행.  
클라이언트는 Reverse Proxy sercer에 요청하기 떄문에 실제 서버에 대해 알 수 없다.  