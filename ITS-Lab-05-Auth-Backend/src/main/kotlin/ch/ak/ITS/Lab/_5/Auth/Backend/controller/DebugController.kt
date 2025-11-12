package ch.ak.ITS.Lab._5.Auth.Backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/debug")
class DebugController {
    companion object {
        const val DEFAULT_PASSWORT = "testPassword"
        const val NANO_SECONDS_IN_SECOND = 1_000_000_000.0
    }

    @GetMapping("/measure-bcrypt-time-with-complexity")
    fun measureBcryptTimeWithComplexity(
        @RequestParam complexity: Int,
        @RequestParam(required = false) testPassword: String?
    ): ResponseEntity<Double> {
        val startTime = System.nanoTime()
        BCryptPasswordEncoder(complexity).encode(testPassword ?: DEFAULT_PASSWORT)
        val endTime = System.nanoTime()

        val elapsedTimeInSeconds = (endTime - startTime) / NANO_SECONDS_IN_SECOND
        return ResponseEntity.ok(elapsedTimeInSeconds)
    }
}
