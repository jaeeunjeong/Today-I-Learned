# Spring WebFlux에서 JPA 사용시 주의 사항
- JPA는 Blocking 기반이기에 WebFlux에서는 어울리지 않음.
- R2DBC를 이용해서 비동기식으로 사용.
## R2DBC
- 비동기 관계형데이터베이스 제공

