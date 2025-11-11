package ch.ak.ITS.Lab._5.Auth.Backend.service.auth

import ch.ak.ITS.Lab._5.Auth.Backend.model.oauth.OAuth2Provider
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService(
    private val userRepository: UserRepository
) : OAuth2UserService<OidcUserRequest, OidcUser> {
    private val delegate = OidcUserService()

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = delegate.loadUser(userRequest)
        val registrationID = OAuth2Provider.valueOf(userRequest.clientRegistration.registrationId.uppercase())
        val attrs = oidcUser.attributes
        val (email, providerID) = when (registrationID) {
            OAuth2Provider.GOOGLE -> attrs["email"] as String to attrs["sub"] as String
        }
        require(email.isNotBlank()) { "Provider returned no email" }

        val existingUser = userRepository.findByEmail(email)
        if (existingUser != null && existingUser.provider != registrationID) {
            throw IllegalStateException("User with email $email already exists for the current provider")
        }
        if (existingUser != null && existingUser.providerID != providerID) {
            throw IllegalStateException("Provider ID mismatch for user with email $email")
        }

        val user = existingUser ?: userRepository.save(
            User(
                email = email,
                nickname = email,
                password = "oauth2user",
                provider = registrationID,
                providerID = providerID,
            )
        )

        val authorities = listOf(SimpleGrantedAuthority(user.role.grantedAuthority))
        return DefaultOidcUser(authorities, oidcUser.idToken, oidcUser.userInfo, "email")
    }
}
