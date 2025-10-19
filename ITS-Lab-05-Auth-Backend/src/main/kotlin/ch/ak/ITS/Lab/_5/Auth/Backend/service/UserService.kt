package ch.ak.ITS.Lab._5.Auth.Backend.service

import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User
import ch.ak.ITS.Lab._5.Auth.Backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun updateNickname(user: User, nickname: String): User {
        if (nickname.isBlank()) {
            throw IllegalArgumentException("Nickname cannot be blank")
        }

        user.nickname = nickname
        return userRepository.save(user)
    }
}
