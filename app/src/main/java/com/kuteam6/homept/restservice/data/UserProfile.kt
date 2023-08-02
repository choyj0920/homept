package com.kuteam6.homept.restservice.data


open class UserProfile(
    var uid: Int,
    var name: String,
    var gender: String,
    var usercategory: String,
    var location: String?,
    var hbti: List<Int>?,

){
    override fun toString(): String {
        return "uid : ${uid} [${name}] ${if(gender=="f" )"여성" else "남성"}, usercategory : ${usercategory}, location: ${location}, hbti : ${hbti} "
    }
}

/**
 * sid 내 세션 int , sessionNow(0 트레이니가 신청한상태 , 1 트레이너 신청 승인)
 * 내 세션 상대가 트레이너 일경우(내가 트레이니) -> 상대 프로필 myTrainerProfile
 * 내 세션 상대가 트레이니 일경우(내가 트레이너) -> 상대 프로필 myTraineeProfile
 */


class MyTrainerProfile(
    uid: Int,
    name: String,
    gender: String,
    usercategory: String,
    location: String?,
    hbti: List<Int>?,

    var trainer_id: Int,
    var career: String?,
    var certificate: String?,
    var lesson: String?,


    ):UserProfile(uid,name,gender,usercategory,location,hbti){
    override fun toString(): String {
        return "[Trainer]" +super.toString() +" 경력 : ${career} , 레슨 : ${lesson} \n"
    }
}

class MyTraineeProfile(
    uid: Int,
    name: String,
    gender: String,
    usercategory: String,
    location: String?,
    hbti: List<Int>?,
    var trainee_id: Int,
    var description: String,



    ):UserProfile(uid,name,gender,usercategory,location,hbti){
    override fun toString(): String {
        return "[Trainee]" +super.toString() +"${description} \n"
    }
}