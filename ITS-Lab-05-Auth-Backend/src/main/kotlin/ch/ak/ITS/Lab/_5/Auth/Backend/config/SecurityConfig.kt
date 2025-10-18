package ch.ak.ITS.Lab._5.Auth.Backend.config

import ch.ak.ITS.Lab._5.Auth.Backend.filter.JwtAuthFilter
import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(12)

    @Bean
    fun authenticationProvider(
        uds: UserDetailsService,
        encoder: PasswordEncoder
    ) = DaoAuthenticationProvider(uds).apply {
        setPasswordEncoder(encoder)
    }

    @Bean
    fun userDetailsService(userRepository: UserRepository) = UserDetailsService { username ->
        userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Username not found")
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration) = config.authenticationManager

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter,
        authenticationProvider: AuthenticationProvider
    ): SecurityFilterChain {
        http.csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                it.anyRequest().authenticated()
            }
            .headers { it.frameOptions { f -> f.disable() } }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}