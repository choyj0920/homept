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
 * 트레이너 프로필 req , res
 */
data class GetTrainerProfileRequest(
    @SerializedName("trainerUid") val trainerUid: Int,
)

data class GetTrainerProfileResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("trainerProfile")
    val trainerProfile: TrainerProfile,
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


/**
 * createPost req , res
 */
data class CreatePostRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("title") val title: String="",
    @SerializedName("content") val content: String="",
    @SerializedName("category") val category: String="000000",
    )

data class CreatePostResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("postid")
    val postid: Int,
)
/**
 * editPost req , res
 */
data class EditPostRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("pid") val pid: Int,
    @SerializedName("title") val title: String="",
    @SerializedName("content") val content: String="",
    @SerializedName("category") val category: String="000000",
)

/**
 * deletePost req , res
 */
data class DeletePostRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("pid") val pid: Int,
)

data class CommonResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
)

/**
 * getPost req , res
 */
data class GetPostRequest(
    @SerializedName("uid") val uid: Int?,
    @SerializedName("category") val pid: String,
)

data class GetPostResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("postlist")
    val postlist: List<Postdata>,
)
data class Postdata(
    @SerializedName("pid")
    val pid: Int,
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("isTrainee")
    val isTrainee: Boolean,
    @SerializedName("postcategory")
    var postcategory: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("create_at")
    val create_at: String,
    @SerializedName("image") // 1이면 이미지 존재,아닐경우 이미지x
    val isImagehave: Int,

){
    override fun toString(): String {
        return "[이미지${if(isImagehave==1)"O" else "X"},${title}]pid(${pid}) 작성자 ${if(isTrainee)"트레이니" else "트레이너"} ${name}(${uid}) 내용: ${content}, 작성일 ${create_at} \n\n"
    }
}


/**
 * createComment req , res
 */
data class CreateCommentRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("pid") val pid: Int,
    @SerializedName("content") val content: String="",
)

data class CreateCommentResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("commentid")
    val commentid: Int,
)
/**
 * deleteComment req , res
 */
data class DeleteCommentRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("cid") val cid: Int,
)

/**
 * editComment req , res
 */
data class EditCommentRequest(
    @SerializedName("uid") val uid: Int,
    @SerializedName("cid") val cid: Int,
    @SerializedName("content") val content: String="",
)


/**
 * getComment req , res
 */
data class GetCommentRequest(
    @SerializedName("pid") val pid: Int,
)
data class GetCommentResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("comments")
    val comments: List<Comment>,
)
data class Comment(
    @SerializedName("pid")
    val pid: Int,
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("isTrainee")
    val isTrainee: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("create_at")
    val create_at: String,

    ){
    override fun toString(): String {
        return "pid(${pid}) 작성자 ${if(isTrainee)"트레이니" else "트레이너"} ${name}(${uid}) 내용: ${content}, 작성일 ${create_at} \n\n"
    }
}




/**
 * createReview req , res
 */
data class CreateReviewRequest(
    @SerializedName("traineruid") val traineruid: Int,
    @SerializedName("traineeuid") val traineeuid: Int,
    @SerializedName("content") val content: String="",
    @SerializedName("score") val score: Double,
)


/**
 * deleteComment req , res
 */
data class DeleteReviewRequest(
    @SerializedName("traineruid") val traineruid: Int,
    @SerializedName("traineeuid") val traineeuid: Int,
)

/**
 * editComment req , res
 */
data class EditReviewRequest(
    @SerializedName("traineruid") val traineruid: Int,
    @SerializedName("traineeuid") val traineeuid: Int,
    @SerializedName("content") val content: String="",
    @SerializedName("score") val score: Double,
)

/**
 * getComment req , res
 */
data class GetReviewRequest(
    @SerializedName("traineruid") val traineruid: Int,
)
data class GetReviewResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("reviews")
    val reviews: List<Review>,
)
data class Review(
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("content")
    val content: String,
    @SerializedName("create_at")
    val create_at: String,

    ){
    override fun toString(): String {
        return "작성자 ${name}(${uid}) 내용: ${content}, 별점 :${score},작성일 ${create_at} \n\n"
    }
}

