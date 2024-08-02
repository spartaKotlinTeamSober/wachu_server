# 테스트 코드를 같이 작성해서 PR을 날릴 것인지? (PR리뷰)

예를 들어
```
// AEntityTest.kt
@Test
fun `hasPermission이 정상 작동하는지 확인`() {
// given
val a = AEntity(/**/)
val principla = UserPrincipal(userid=1, role="ROLE_USER")

// when
val result = a.hasPermission(priciple.id)

// then
result shouldBe true
}

// AServiceTest.kt
class ATestRepositoryImpl: ARepository {
	fun findById(request: DTO): A {
		return A() // test stub
	}
}

val service = AServiceImpl(ATestRepositoryImpl) 
// Service 말고 Repository를 대역을 생성, 
// 서비스 논리에 대한 테스트가 주요 목적이니 다른 대상은 관심 외다


// DB 논리까지 검증이 필요할 경우 실제 DB 의존성을 사용한다
or @Autowired service: AService


@Test
fun `1L을 조회하려고 할 때 실패한다`() {
// given
val id = 1L
val request = ACreateRequest(id)

// when
val result = shouldThrow service.create(request) // 이 내부에 DTO의 값으로 Throw 하는 과정이 있었다면 반드시 테스트

// then
result.name shouldBe "123"
}

```

같은 식의 통합 테스트 / 유닛 테스트를 작성해서 성공과 실패 케이스를 만들어본다. 

QueryDSL의 쿼리 테스트라면 Repository Test 작성
제안 이유: 테스트 코드 이슈를 나중에서야 따로 파가지고 누가 담당하는건 항상 후순위로 밀려났고 큰 의미를 가지지도 못했다

뻔한 로직이 될 수도 있고 로직 자체가 지나치게 쉬울 수도 있지만
지금이라도 작성하면서 PR을 날리는 습관을 유지할 지에 대해 의사결정을 해봅시다

유닛 테스트는 애매할 수 있으니 차라리 통합테스트를 하는게 나을 수도 있다
동의한다면: 리뷰때 테스트 코드 작성할 부분이 빠져있는지 등도 체크를 하게됨
비동의한다면: 테스트 할사람만 적고 안할사람 안적는걸로 (필수로 리뷰안함)

예를 들어: 
JwtProvider를 만들었을 때, 검증 로직까지 바로 테스트 코드를 작성하는지 아닌지를 리뷰때 작성하는건 어떨까요? 같은 내용을 Conversation으로 남길지, 아니면 그냥 넘길지 등을 결정한다
결론: 테스트 코드도 최대한 해당 이슈에서 발생할만한 여지가 있는지 생각해보면서 진행하기로 했다. 리뷰에서도 한번씩 짚어보면서 넘어가자

