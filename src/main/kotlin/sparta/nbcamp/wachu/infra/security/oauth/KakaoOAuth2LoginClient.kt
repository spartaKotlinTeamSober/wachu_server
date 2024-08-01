package sparta.nbcamp.wachu.infra.security.oauth

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
import sparta.nbcamp.wachu.infra.security.oauth.dto.KakaoOAuthLoginUserInfoResponse
import sparta.nbcamp.wachu.infra.security.oauth.dto.KakaoTokenResponse

@Component
class KakaoOAuth2LoginClient(
    @Value("\${oauth2.kakao.client_id}") val clientId: String,
    @Value("\${oauth2.kakao.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.kakao.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.kakao.resource_server_base_url}") val resourceServerBaseUrl: String,
    private val restClient: RestClient
) {
    fun generateLoginPageUrl(): String {
        return StringBuilder(authServerBaseUrl)
            .append("/oauth/authorize")
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    fun getAccessToken(code: String): String {
        val requestData = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUrl)
            add("code", code)
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val request = HttpEntity(requestData, headers)

        return try {
            val response: ResponseEntity<KakaoTokenResponse> = RestTemplate().exchange(
                "$authServerBaseUrl/oauth/token",
                HttpMethod.POST,
                request,
                KakaoTokenResponse::class.java
            )

            if (response.statusCode.is2xxSuccessful) {
                response.body?.accessToken ?: throw RuntimeException("카카오 AccessToken 조회 실패: 응답 본문이 비어있음")
            } else {
                println("Error fetching access token: ${response.statusCode} - ${response.body}")
                throw RuntimeException("카카오 AccessToken 조회 실패: ${response.statusCode}")
            }
        } catch (e: Exception) {
            println("Exception occurred while fetching access token: ${e.message}")
            throw RuntimeException("카카오 AccessToken 조회 실패: ${e.message}", e)
        }
    }

    fun retrieveUserInfo(accessToken: String): KakaoOAuthLoginUserInfoResponse {
        return restClient.get()
            .uri("$resourceServerBaseUrl/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ ->
                throw RuntimeException("카카오 UserInfo 조회 실패")
            }
            .body<KakaoOAuthLoginUserInfoResponse>()
            ?: throw RuntimeException("카카오 UserInfo 조회 실패")
    }
}