package com.kuteam6.homept.restservice.data

import com.google.gson.annotations.SerializedName
import java.util.*


/**
 * 중복 체크 req , res
 */
data class CheckIdRequest(
    @SerializedName("id") val id: String)
data class CheckIdResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("isDuplicated")
    val isDuplicated: Boolean
)

/**
 * 회원가입 req , res
 */
data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("isTrainee") val isTrainee: Boolean,
    @SerializedName("usercategory") val usercategory: String,
    @SerializedName("location") val location: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("career") val career: String?,
    @SerializedName("lesson") val lesson: String?,
)

fun createRegisterReq(user: UserData) :RegisterRequest{
    return RegisterRequest(
        name = user.name,
        id = user.id,
        password = user.password,
        gender = user.gender,
        birth = user.birth.toString(),
        isTrainee = user.isTrainee,
        usercategory = user.userCategory,
        location = user.location,
        description = if(user is TraineeData) (user as TraineeData).description else null,
        career = if(user is TrainerData) (user as TrainerData).career else null,
        lesson = if(user is TrainerData) (user as TrainerData).lesson else null,
    )
}
data class RegisterResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("subuid")
    val subuid: Int?,

    )


/**
 * 로그인 req , res
 */
data class LoginRequest(
    @SerializedName("login_id") val login_id: String,
    @SerializedName("password") val password: String
)
data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("userdata")
    val userdata: LoginUserData?,
    @SerializedName("subuserdata")
    val subuserdata: SubUserData?,
)

data class LoginUserData(
    @SerializedName("uid") val uid: Int,
    @SerializedName("login_id") val login_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("role") val role: String,
    @SerializedName("usercategory") val usercategory: String,
    @SerializedName("location") val location: String?,
    @SerializedName("register_date") val registerDate: String,
    @SerializedName("login_date") val loginDate: String,


    )

/**
 * 현재 이 데이터에는 phone,certificate,kkt,email등의 변수가 있으나 이것을 입력하는 부분이 클라이언트에 없어서 항상 null -추후 수정 필요
 */
data class SubUserData(
    @SerializedName("trainee_id") val trainee_id: Int?,
    @SerializedName("trainer_id") val trainer_id: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("career") val career: String?,
    @SerializedName("lesson") val lesson: String?,
    @SerializedName("certificate") val certificate: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("kkt") val kkt: String?,
    @SerializedName("email") val email: String?,
)


/**
 * 회원탈퇴 req , res
 */
data class UnRegisterRequest(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String
)
data class UnRegisterResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,

    )

/**
 * 비밀번호 찾기 req , res
 */
data class PasswordFindRequest(
    @SerializedName("login_id") val login_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("newpassword") val newpassword: String? =null
)

data class PasswordFindResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Boolean,

    )
