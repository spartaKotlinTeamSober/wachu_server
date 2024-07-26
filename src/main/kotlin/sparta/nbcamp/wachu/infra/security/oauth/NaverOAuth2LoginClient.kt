package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.body
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthLoginUserInfoResponse
import sparta.nbcamp.wachu.infra.security.oauth.dto.TokenResponse
import java.util.UUID

@Component
class NaverOAuth2LoginClient(
    @Value("\${oauth2.naver.clientSecret}") val clientSecret: String,
    @Value("\${oauth2.naver.client_id}") val clientId: String,
    @Value("\${oauth2.naver.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.naver.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.naver.resource_server_base_url}") val resourceServerBaseUrl: String,
    private val restClient: RestClient
) {
    fun generateLoginPageUrl(session: HttpSession): String {
        val state = UUID.randomUUID().toString()
        session.setAttribute("oauthState", state)

        return StringBuilder(authServerBaseUrl)
            .append("/oauth2.0/authorize")
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&response_type=code")
            .append("&state=").append(state)
            .toString()
    }

    fun getAccessToken(code: String, state: String): String {
        val requestData = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("client_secret", clientSecret) // client_secret 추가
            add("code", code)
            add("state", state) // state 추가
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val request = HttpEntity(requestData, headers)

        return try {
            val response: ResponseEntity<TokenResponse> = RestTemplate().exchange(
                "$authServerBaseUrl/oauth2.0/token",
                HttpMethod.POST,
                request,
                TokenResponse::class.java
            )

            if (response.statusCode.is2xxSuccessful) {
                response.body?.accessToken ?: throw RuntimeException("네이버 AccessToken 조회 실패: 응답 본문이 비어있음")
            } else {
                println("Error fetching access token: ${response.statusCode} - ${response.body}")
                throw RuntimeException("네이버 AccessToken 조회 실패: ${response.statusCode}")
            }
        } catch (e: Exception) {
            println("Exception occurred while fetching access token: ${e.message}")
            throw RuntimeException("네이버 AccessToken 조회 실패: ${e.message}", e)
        }
    }

    fun retrieveUserInfo(accessToken: String): OAuthLoginUserInfoResponse {
        return restClient.get()
            .uri("$resourceServerBaseUrl/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw RuntimeException("네이버 UserInfo 조회 실패")
            }
            .body<OAuthLoginUserInfoResponse>()
            ?: throw RuntimeException("네이버 UserInfo 조회 실패")
    }
}