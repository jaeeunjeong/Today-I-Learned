# Builder 
- 정의
  1. 객체를 다양한 경우에 수에 대응하기 위해 만들어진 패턴
  2. 복합 객체가 생성되는 과정을 캡슐화하여 사용함. 
- 장점
  1. 어느 필드에 어떤 값을 채워 넣을 지에 대해 정확하게 인지 가능
  2. 여러 단계와 다양한 절차를 이용하여 객체의 생성이 가능하다.
  3. 제품 내부 구조를 클라이언트로부터 보호할 수 있다.
- 단점 : 객체를 사용하기 위해 객체에 대한 정보를 많이 알아야 한다.
- 구현 방법
```
public final class PostsBuilder {
    Long postSeq;
    String title;
    String content;
    String writer;

    private PostsBuilder() {
    }

    public static PostsBuilder aPosts() {
        return new PostsBuilder();
    }

    public PostsBuilder withPostSeq(Long postSeq) {
        this.postSeq = postSeq;
        return this;
    }

    public PostsBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostsBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public PostsBuilder withWriter(String writer) {
        this.writer = writer;
        return this;
    }

    public PostsBuilder but() {
        return aPosts().withPostSeq(postSeq).withTitle(title).withContent(content).withWriter(writer);
    }

    public Posts build() {
        Posts posts = new Posts();
        posts.setPostSeq(postSeq);
        posts.setTitle(title);
        posts.setContent(content);
        posts.setWriter(writer);
        return posts;
    }
}
```
```
    PostsBuilder.aPosts().withTitle("가입 인사").withContent("안녕하세요.").withWriter("방문자").build();
```
위와 같은 방식으로 사용.  
- Lombok의 **@Bulider** 가 이를 구현해주는 annotation이다.  
## 
- 프록시 패턴 : 설정과 같은 용도로 공통화 하는 것
- 데코레이터 패턴 : 기능을 추가하고자 공통화 하는 것
