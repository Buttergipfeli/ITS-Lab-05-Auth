package ch.ak.ITS.Lab._5.Auth.Backend.repository

import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}
