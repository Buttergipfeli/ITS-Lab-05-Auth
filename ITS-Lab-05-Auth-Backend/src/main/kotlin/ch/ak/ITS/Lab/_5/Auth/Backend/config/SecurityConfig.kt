package ch.ak.ITS.Lab._5.Auth.Backend.config

import ch.ak.ITS.Lab._5.Auth.Backend.filter.JwtAuthFilter
import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

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
    fun userDetailsService(userRepository: UserRepository) = UserDetailsService { email ->
        userRepository.findByEmail(email) ?: throw UsernameNotFoundException("Email not found")
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        return UrlBasedCorsConfigurationSource().apply {
            val c = CorsConfiguration().apply {
                allowedOrigins = listOf(
                    "http://127.0.0.1:5500",
                    "http://localhost:5500"
                )
                allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                allowedHeaders = listOf("Authorization", "Content-Type")
                maxAge = 3600
            }
            registerCorsConfiguration("/**", c)
        }
    }

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
                it.requestMatchers("/api/user/**").hasRole("USER")
                it.anyRequest().denyAll()
            }
            .headers { it.frameOptions { f -> f.disable() } }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

        return http.build()
    }
}
