package ch.ak.ITS.Lab._5.Auth.Backend.controller

import ch.ak.ITS.Lab._5.Auth.Backend.dto.user.toUserResponse
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController {
    @GetMapping("/info")
    fun getUserInfo(@AuthenticationPrincipal user: User) =
        ResponseEntity.ok(user.toUserResponse())
}
