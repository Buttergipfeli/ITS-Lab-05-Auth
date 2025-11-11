package ch.ak.ITS.Lab._5.Auth.Backend.config.reflection_hints

import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.util.ClassUtils
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory

class GreedyLibRuntimeHints : RuntimeHintsRegistrar {

    companion object {
        private val LIB_COORDS = listOf(
            "io.jsonwebtoken:jjwt-api",
            "io.jsonwebtoken:jjwt-impl",
            "io.jsonwebtoken:jjwt-jackson"
        )

        private val KEEP_PACKAGE_PREFIXES =
            LIB_COORDS.mapNotNull { it.substringBefore(':', "").trim().ifEmpty { null } }.distinct()

        private val CATEGORIES = arrayOf(
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS,
            MemberCategory.PUBLIC_FIELDS
        )
    }

    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        if (KEEP_PACKAGE_PREFIXES.isEmpty()) return

        val loader = classLoader ?: Thread.currentThread().contextClassLoader
        val resolver = PathMatchingResourcePatternResolver(loader)
        val mrf = CachingMetadataReaderFactory(resolver)

        fun registerPackage(pkgPrefix: String) {
            val pattern = "classpath*:${ClassUtils.convertClassNameToResourcePath(pkgPrefix)}/**/*.class"
            resolver.getResources(pattern).forEach { res ->
                val fqcn = mrf.getMetadataReader(res).classMetadata.className
                hints.reflection().registerType(TypeReference.of(fqcn), *CATEGORIES)
            }
        }

        KEEP_PACKAGE_PREFIXES.forEach(::registerPackage)
    }
}
