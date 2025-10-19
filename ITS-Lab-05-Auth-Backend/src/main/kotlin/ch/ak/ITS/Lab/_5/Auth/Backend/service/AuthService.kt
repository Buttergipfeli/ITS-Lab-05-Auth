package ch.ak.ITS.Lab._5.Auth.Backend.service

import ch.ak.ITS.Lab._5.Auth.Backend.dto.auth.AuthRequest
import ch.ak.ITS.Lab._5.Auth.Backend.dto.auth.RegisterRequest
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.Role
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwt: JwtService,
    private val authManager: AuthenticationManager
) {
    fun register(req: RegisterRequest): String {
        require(req.email.isNotBlank() && req.password.isNotBlank()) { "Email and password must not be blank" }
        require(!userRepository.existsByEmail(req.email)) { "Email already exists" }

        val user = User(
            email = req.email,
            nickname = req.nickname,
            password = encoder.encode(req.password),
            role = Role.USER
        )
        userRepository.save(user)
        return jwt.generateToken(user)
    }

    fun login(req: AuthRequest): String {
        val token = UsernamePasswordAuthenticationToken(req.email, req.password)
        authManager.authenticate(token)

        val user = userRepository.findByEmail(req.email) ?: throw IllegalArgumentException("User not found")
        return jwt.generateToken(user)
    }
}
