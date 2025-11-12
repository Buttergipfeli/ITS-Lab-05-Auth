package ch.ak.ITS.Lab._5.Auth.Backend

import ch.ak.ITS.Lab._5.Auth.Backend.config.reflection_hints.GreedyLibRuntimeHints
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@ImportRuntimeHints(GreedyLibRuntimeHints::class)
@SpringBootApplication
class ItsLab05AuthBackendApplication

fun main(args: Array<String>) {
	runApplication<ItsLab05AuthBackendApplication>(*args)
}
