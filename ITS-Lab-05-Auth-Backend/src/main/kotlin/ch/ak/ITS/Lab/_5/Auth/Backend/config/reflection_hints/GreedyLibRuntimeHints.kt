package ch.ak.ITS.Lab._5.Auth.Backend.config.reflection_hints

import org.springframework.aot.hint.*
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.util.ClassUtils
import java.io.BufferedReader
import java.nio.charset.StandardCharsets

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

        fun registerPackageRecursively(pkgPrefix: String) {
            val pattern =
                "classpath*:${ClassUtils.convertClassNameToResourcePath(pkgPrefix)}/**/*.class"
            resolver.getResources(pattern).forEach { res ->
                val md = mrf.getMetadataReader(res)
                val fqcn = md.classMetadata.className

                hints.reflection().registerType(TypeReference.of(fqcn), *CATEGORIES)

                if (md.classMetadata.interfaceNames.contains("java.io.Serializable")) {
                    hints.serialization().registerType(TypeReference.of(fqcn))
                }
            }
        }
        KEEP_PACKAGE_PREFIXES.forEach(::registerPackageRecursively)

        KEEP_PACKAGE_PREFIXES.forEach { pkg ->
            val path = "classpath*:${ClassUtils.convertClassNameToResourcePath(pkg)}/**/META-INF/services/*"
            resolver.getResources(path).forEach { svc ->
                registerServiceFile(hints, svc)
            }
        }

        KEEP_PACKAGE_PREFIXES.forEach { pkg ->
            val base = ClassUtils.convertClassNameToResourcePath(pkg)
            hints.resources().registerPattern("$base/")  // Ganzes Package
            hints.resources().registerPattern("$base/*.properties")
            hints.resources().registerPattern("$base/*.yml")
            hints.resources().registerPattern("$base/*.yaml")
            hints.resources().registerPattern("META-INF/spring/**")
            hints.resources().registerPattern("META-INF/native-image/**")
        }

        hints.resources().registerPattern("META-INF/spring/**")
        hints.resources().registerPattern("application*.properties")
        hints.resources().registerPattern("application*.yml")
    }

    private fun registerServiceFile(hints: RuntimeHints, svc: Resource) {
        val name = svc.filename ?: return
        hints.resources().registerPattern("META-INF/services/$name")

        svc.inputStream.bufferedReader(StandardCharsets.UTF_8).use(BufferedReader::readLines)
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .forEach { providerFqcn ->
                hints.reflection().registerType(
                    TypeReference.of(providerFqcn),
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS
                )
            }
    }
}
