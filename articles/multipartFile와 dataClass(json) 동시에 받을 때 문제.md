`MultipartFile` 를 써야 파일을 잘 전송 할 수 있다고 함. 이것을 잘 쓸 수 있게 해주는게 `RequestPart` 인데, 이게 dataClass랑 같이 엮어서 보내려 하니 계속 data-format 오류가 남. 

이에 대한 해결책으로 

```jsx
@Component
class MultipartJson2HttpMessageConverter(
    private val objectMapper: ObjectMapper
): AbstractJackson2HttpMessageConverter(objectMapper,MediaType.APPLICATION_OCTET_STREAM){

    @Override
    override fun canWrite(mediaType: MediaType?): Boolean {
        return false
    }

    @Override
    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }

    @Override
    override fun canWrite(type: Type?, clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }
}
```

### MultipartJson2HttpMessageConverter의 역할

- `canWrite` 메서드들이 모두 `false`를 반환하도록 오버라이드 하여. 어떤 경우에도 쓰기 작업을 수행하지 않도록 설정.
- `objectMapper`를 사용하여 `AbstractJackson2HttpMessageConverter`를 초기화하고, `MediaType.APPLICATION_OCTET_STREAM` 미디어 타입을 사용하도록 설정.

.
