package ch.ak.ITS.Lab._5.Auth.Backend.config.reflection_hints

import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class JjwtRuntimeHints: RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val typesNeedingNoArgCtor = listOf(
            "io.jsonwebtoken.impl.DefaultJwtHeaderBuilder\$Supplier",
            "io.jsonwebtoken.impl.DefaultJwtBuilder\$Supplier",
            "io.jsonwebtoken.impl.DefaultClaimsBuilder\$Supplier",
            "io.jsonwebtoken.impl.DefaultJwtParserBuilder\$Supplier",
            "io.jsonwebtoken.impl.security.DefaultKeyOperationBuilder\$Supplier",
            "io.jsonwebtoken.impl.security.DefaultKeyOperationPolicyBuilder\$Supplier",
            "io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms",
            "io.jsonwebtoken.impl.security.StandardKeyOperations",
            "io.jsonwebtoken.impl.security.KeysBridge",
            "io.jsonwebtoken.impl.security.StandardEncryptionAlgorithms",
            "io.jsonwebtoken.impl.security.StandardKeyAlgorithms",
            "io.jsonwebtoken.impl.io.StandardCompressionAlgorithms"
        )

        val reflection = hints.reflection()
        typesNeedingNoArgCtor.forEach { fqcn ->
            reflection.registerType(
                TypeReference.of(fqcn),
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
            )
        }
    }
}
