package sparta.nbcamp.wachu.domain.member.dto

data class SocialSignUpRequest(
    val nickname: String, // nickname을 넣은 이유는 동일한 소셜로그인으로 회원가입한 유저의 nickname이 기존 서비스 회원의 nickname과 중복됬을 경우를 고려했기 때문 
    val email: String,
)
