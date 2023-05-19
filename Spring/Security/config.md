# 스프링 시큐리티 설정하기
```java

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
public class SecurityConfig {

    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        httpSecurity
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/verify").permitAll()
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/user").permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
            .addFilterBefore(new JwtTokenFilter(userService), UsernamePasswordAuthenticationFilter.class)
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

spring security가 5.7 버전이 이후 WevSecurityConfigurerAdapter를 이용하여 구현하는 것이 아닌 SecurityFilterChain를 리턴하는 빈을 등록하여 생성하는 방식으로 변경되었다.  

이에 따라 에러가 발생하게 되는데, security에서는 defalut로 SecurityFilterChain를 등록하는데 중복으로 등록하게 되어 경고가 발생하게 된다.

따라서, 

클래스에는 
```java
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
```
filter method에는 
```@Order(SecurityProperties.BASIC_AUTH_ORDER)```

### 새로운 시큐리티에서 변경된 점
1. 접근시 인증이 필요한 부분/ 필요하지 않은 부분 설정을 위해 사용
 ```
 .authorizeRequests()
 .antMatchers(HttpMethod.POST, "/user").permitAll().anyRequest().authenticated();
 ```
 ->
```
.authorizeHttpRequests
.requestMatchers(HttpMethod.POST, "/user").permitAll().anyRequest().authenticated()
```
 
