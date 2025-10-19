package ch.ak.ITS.Lab._5.Auth.Backend.dto.user

import ch.ak.ITS.Lab._5.Auth.Backend.model.user.Role
import ch.ak.ITS.Lab._5.Auth.Backend.model.user.User

data class UserInfoResponse(
    val email: String,
    val nickname: String,
    val role: Role
)

fun User.toUserInfoResponse() = UserInfoResponse(
    email = username,
    nickname = nickname,
    role = role
)
