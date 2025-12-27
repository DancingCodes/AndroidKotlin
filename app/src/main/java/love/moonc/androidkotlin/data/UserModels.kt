package love.moonc.androidkotlin.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("account") val account: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("signature") val signature: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class RegisterRequest(
    val nickname: String,
    val account: String,
    val password: String
)

data class LoginRequest(
    val account: String,
    val password: String
)

data class AuthData(
    val token: String
)

data class UpdateUserRequest(
    val password: String? = null,
    val nickname: String? = null,
    val avatar: String? = null,
    val signature: String? = null
)

data class UserProfile(
    val user: User
)