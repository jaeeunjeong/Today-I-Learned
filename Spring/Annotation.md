# Annotation

## @RequestParam vs @PathVariable
- 둘 다 파라미터를 전달받기 위한 어노테이션
- @RequestParam의 경우 쿼리, form의 data, multipart의 data 등의 값을 전달받고,  
- @PathVariable RESTful URL 주소 형태의 parameter 값을 전달받는다. 
- 공통적으로 required() 속성을 통해 파라미터의 값이 필수인지 아닌지 정할 수 있으며, default는 true이다.  
 false로 정하면 파라미터가 null이어도 오류가 발생하지 않는다. 
### @PathVariable
```
  @GetMapping("communuty/read/{postId}")
  public Posts readPost(@PathVariable int postId){
      return postsService.readPost(postId);
  }
```
- URI 템플릿 변수와 메서드 파라미터가 맵핑되어 사용한다.  
- @RequestMapping 계열의 annotation에서 사용한다.(@PostMapping/@GetMapping)  

### @RequestParam 
```
  @GetMapping("communuty/home")
  public List<Posts> selectPostsList(@RequestParam (required = false) int page){
      return postsService.selectPostsList();
  }
```
- web에서 요청하는 파라미터를 맵핑하여 사용된다.
- post 형태로 보내는 파라미터, form 형태의 파라미터, Multipart 형태의 파라미터가 맵핑된다.


### 참고
spring document
