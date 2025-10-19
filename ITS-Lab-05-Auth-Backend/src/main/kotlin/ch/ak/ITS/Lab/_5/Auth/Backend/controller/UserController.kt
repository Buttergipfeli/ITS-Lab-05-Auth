package ch.ak.ITS.Lab._5.Auth.Backend.controller

import ch.ak.ITS.Lab._5.Auth.Backend.dto.user.UserInfoResponse
import ch.ak.ITS.Lab._5.Auth.Backend.dto.user.UserNicknameChangeRequest
import ch.ak.ITS.Lab._5.Auth.Backend.dto.user.toUserInfoResponse
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import ch.ak.ITS.Lab._5.Auth.Backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
    @GetMapping
    fun getUserInfo(@AuthenticationPrincipal user: User) =
        ResponseEntity.ok(user.toUserInfoResponse())

    @PutMapping
    fun updateNickname(@AuthenticationPrincipal user: User, @RequestBody req: UserNicknameChangeRequest):
            ResponseEntity<UserInfoResponse> {
        val updatedUser = userService.updateNickname(user, req.nickname)
        return ResponseEntity.ok(updatedUser.toUserInfoResponse())
    }
}
