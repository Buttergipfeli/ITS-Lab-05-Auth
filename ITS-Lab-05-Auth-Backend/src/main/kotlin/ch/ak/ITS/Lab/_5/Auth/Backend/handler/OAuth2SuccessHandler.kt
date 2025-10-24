package ch.ak.ITS.Lab._5.Auth.Backend.handler

import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import ch.ak.ITS.Lab._5.Auth.Backend.service.auth.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    @Value("\${frontend.url}") private val frontendUrl: String
) : AuthenticationSuccessHandler {
    companion object {
        const val FRONTEND_REDIRECT_PATH = "/oauth2/redirect"
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as OAuth2User
        val email = principal.attributes["email"] as String
        val user = userRepository.findByEmail(email)
            ?: throw IllegalStateException("User with email $email not found in database")

        val token = jwtService.generateToken(user)

        val redirectUrl = "$frontendUrl$FRONTEND_REDIRECT_PATH#token=$token"
        response.sendRedirect(redirectUrl)
    }
}
