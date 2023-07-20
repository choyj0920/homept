package com.kuteam6.homept.restservice.data

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * userCategory ="111111" 형태로 입력 각 자리가 1번태그check ,2번태그 check,...6번태그 check
 * 1번만 check => "100000"
 * 가입전에는 uid가 -1로 지정되잇지 않으나 가입함수에서 리턴되면 uid가 지정
 */
open class UserData(
    val name: String,
    val id: String,
    val password: String,
    val gender: String,
    val birth: LocalDate,
    val isTrainee: Boolean,
    var userCategory: String,
    var location: String?=null,
    var uid:Int=-1,
    var registerDate:LocalDate?=null,

    ){

    override fun toString(): String {

        return "uid : ${uid} 이름 :${name}, id: ${id}, password : ${password}, gender : ${gender}, birth : ${birth}, usercategroy :${userCategory},가입일자 : ${registerDate} location : ${location}"
    }
}
/**
 * userCategory ="111111" 형태로 입력 각 자리가 1번태그check ,2번태그 check,...6번태그 check
 * 1번만 check => "100000" ,uid는 직접입력x
 * 트레이너는 location 입력
 */
class TrainerData(
    name: String,
    id: String,
    password: String,
    gender: String,
    birth: LocalDate,
    isTrainee: Boolean,
    userCategory: String,
    var career: String,
    var lesson: String,
    var trainerid:Int=-1,
    location: String?=null,
    var certificate: String?=null,


    ) : UserData(name, id, password, gender, birth, isTrainee, userCategory,location){
    override fun toString(): String {
        return "[트레이너] ${super.toString()}, 이력 : ${career} , lesson : ${lesson}, 자격사항 : ${certificate}"
    }
}

/**
 * userCategory ="111111" 형태로 입력 각 자리가 1번태그check ,2번태그 check,...6번태그 check
 * 1번만 check => "100000" ,uid는 직접입력x
 * 트레이니는 location안쓸 예정
 */
class TraineeData(
    name: String,
    id: String,
    password: String,
    gender: String,
    birth: LocalDate,
    isTrainee: Boolean,
    userCategory: String,
    var description: String,
    var traineeid:Int=-1,
    //    location: String?=null,

) : UserData(name, id, password, gender, birth, isTrainee, userCategory/*,location*/){
    override fun toString(): String {
        return "[트레이니] ${super.toString()}, description : ${description}"
    }
}




