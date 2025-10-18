package ch.ak.ITS.Lab._5.Auth.Backend.dto.user

import ch.ak.ITS.Lab._5.Auth.Backend.model.user.Role
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User

data class UserResponse(
    val email: String,
    val role: Role
)

fun User.toUserResponse() = UserResponse(
    email = username,
    role = role
)
