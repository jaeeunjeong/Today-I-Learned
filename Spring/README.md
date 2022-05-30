# Spring
> 자바의 프레임워크의 하나로, 프로그램에서 사용되는 인스턴스들을 이 프레임워크에서 관리해줘서 편리한 사용이 가능하다.
## SpringBoot
> Spring의 설정을 편리하게 해주고 tomcat이 내장되어 다양한 환경에서 사용이 가능하다.
## bean
> 스프링에서 사용하는 객체
> SpringContainer에 등록해서 사용된다. -> 싱글톤 패턴이 사용됨.
### Spring Container
스프링 객체인 빈이 관리되는 곳으로 이 곳에 Bean이 등록되면 이 빈은 한 번 생성되면 다른 곳에서 이 빈을 주입받아서 사용이 가능하다.  
*주입 : 현재 사용중인 인스턴스에서 다른 인스턴스와 호출하여 같이 사용하는 것.*
### Singletone pattern
인스턴스를 한 번만 생성하고 생성된 그 객체를 계속해서 다른 곳에서 사용하는 것을 의미  
## Spring framework의 원리 
1. Dispatcher Servlet이 request를 받으면 Handler Mapping을 통해 URL을 전달받는다.
2. 전달받은 URL을 Contoller로 보낸다.
3. Controller은 Model을 완성시켜서 Dispatcher Servlet으로 보낸다.
4. Dispatcher Servlet는 View Resolver를 통해 request에 맞는 View를 찾고 클라이언트에게 응답할 페이지를 만든다.
5. 클라이언트에게 응답한다.
