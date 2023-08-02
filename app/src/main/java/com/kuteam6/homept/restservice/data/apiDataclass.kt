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
    @SerializedName("hbti") val hbti: List<Int>?,

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



/**
 * 트레이너 찾기 req , res
 */
data class TrainerSearchRequest(
    @SerializedName("category") val category: String,
    // m or f
    @SerializedName("gender") val gender: String?,
    @SerializedName("location") val location: String,
)

data class TrainerSearchResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("trainerlist")
    val trainerlist: List<TrainerProfile>,
)
/**
 * get Hbti req, res
 */
data class GetHbtiRequest(
    @SerializedName("uid") val uid: Int,
)

data class GetHbtiResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("hbti")
    val hbti: List<Int>?,
)
/**
 * set Hbti req, res
 */
data class SetHbtiRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("hbti") val hbti: List<Int>?,
)

data class SetHbtiResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
)



/**
 * 트레이너 추천 req , res
 */
data class RecommendTrainerRequest(
    @SerializedName("category") val category: String,
    // m or f
    @SerializedName("gender") val gender: String?,
    @SerializedName("location") val location: String,
    @SerializedName("hbti") val hbti: List<Int>?,

)

data class RecommendTrainerResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("trainerlist")
    val trainerlist: List<TrainerProfile>,
)

/**
 * 매칭 신청 req , res
 */
data class ApplySessionRequest(
    @SerializedName("traineeUid") val traineeUid: Int,
    @SerializedName("trainerUid") val trainerUid: Int,
    @SerializedName("trainee_memo") val trainee_memo: String="",

    )

data class ApplySessionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("isSucess")
    val isSucess: Boolean,
)

/**
 * 매칭 승인 req , res
 */
data class ApproveSessionRequest(
    @SerializedName("trainerUid") val trainerUid: Int,
    @SerializedName("sid") val sid: Int,
    @SerializedName("trainee_memo") val trainee_memo: String="",

    )

data class ApproveSessionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("isSucess")
    val isSucess: Boolean,
)

/**
 * 내 트레이니 리스트 req , res
 */
data class GetMyTraineesRequest(
    @SerializedName("trainerUid") val trainerUid: Int,

    )
data class GetMyTraineesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("traineelist")
    val traineelist: List<_myTrainee>,
    ){
    fun toList():List<MySession>{
        return traineelist.map {
            MySession(sid= it.sid, myTraineeProfile =  MyTraineeProfile(
                it.uid,it.name,it.gender,it.usercategory,it.location,it.hbti,it.trainee_id,it.description ),
                sessionNow= it.sessionnow);

        }
    }
}
data class _myTrainee(
    @SerializedName("sid")
    val sid: Int,
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("sessionnow")
    val sessionnow: Int,
    @SerializedName("trainee_id")
    val trainee_id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    val gender: String,

    @SerializedName("description")
    val description: String,
    @SerializedName("usercategory")
    var usercategory: String,
    @SerializedName("location")
    val location: String?,

    @SerializedName("hbti")
    val hbti: List<Int>?,
)

/**
 * 내 트레이너 리스트 req , res
 */
data class GetMyTrainersRequest(
    @SerializedName("traineeUid") val traineeUid: Int,

    )
data class GetMyTrainersResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("trainerlist")
    val trainerlist: List<_myTrainer>,
){
    fun toList():List<MySession>{
        return trainerlist.map {
            MySession(sid= it.sid, myTrainerProfile =  MyTrainerProfile(
                it.uid,it.name,it.gender,it.usercategory,it.location,it.hbti,it.trainer_id,it.career,it.certificate,it.lesson ),
                sessionNow= it.sessionnow
            );

        }
    }
}
data class _myTrainer(
    @SerializedName("sid")
    val sid: Int,
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("sessionnow")
    val sessionnow: Int,
    @SerializedName("trainer_id")
    val trainer_id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    val gender: String,

    @SerializedName("career")
    val career: String?,
    @SerializedName("certificate")
    val certificate: String?,
    @SerializedName("lesson")
    val lesson: String?,
    @SerializedName("usercategory")
    var usercategory: String,
    @SerializedName("location")
    val location: String?,

    @SerializedName("hbti")
    val hbti: List<Int>?,
)