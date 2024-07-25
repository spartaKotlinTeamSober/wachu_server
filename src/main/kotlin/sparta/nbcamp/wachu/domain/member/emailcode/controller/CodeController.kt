package sparta.nbcamp.wachu.domain.member.emailcode.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.emailcode.dto.CheckCodeRequest
import sparta.nbcamp.wachu.domain.member.emailcode.dto.SendCodeRequest
import sparta.nbcamp.wachu.domain.member.emailcode.service.CodeService

@RequestMapping("/auth/sign-up")
@RestController
class CodeController(
    private val codeService: CodeService,
) {
    @PostMapping("/send-code")
    fun sendCode(@RequestBody request: SendCodeRequest): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(codeService.sendCode(request.email))
    }

    @PostMapping("/check-code")
    fun checkCode(@RequestBody request: CheckCodeRequest): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(codeService.checkCode(request.email, request.code))
    }
}