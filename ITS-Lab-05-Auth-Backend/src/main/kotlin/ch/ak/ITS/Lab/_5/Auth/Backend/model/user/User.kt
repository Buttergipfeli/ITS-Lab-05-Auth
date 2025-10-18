package ch.ak.ITS.Lab._5.Auth.Backend.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "app_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    private var email: String,

    @Column(nullable = false)
    private var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER
) : UserDetails {
    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    override fun getPassword() = password
    override fun getUsername() = email
}
