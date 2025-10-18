package ch.ak.ITS.Lab._5.Auth.Backend.controller

import ch.ak.ITS.Lab._5.Auth.Backend.dto.auth.AuthRequest
import ch.ak.ITS.Lab._5.Auth.Backend.dto.auth.AuthResponse
import ch.ak.ITS.Lab._5.Auth.Backend.dto.auth.RegisterRequest
import ch.ak.ITS.Lab._5.Auth.Backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val auth: AuthService) {
    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest) =
        ResponseEntity.ok(AuthResponse(auth.register(req)))

    @PostMapping("/login")
    fun login(@RequestBody req: AuthRequest) =
        ResponseEntity.ok(AuthResponse(auth.login(req)))
}
