package com.kuteam6.homept.restservice.data

import com.google.gson.annotations.SerializedName


data class TrainerProfile(
    @SerializedName("uid")
    val uid: Int,
    @SerializedName("trainer_id")
    val trainer_id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("gender")
    var gender: String,

    @SerializedName("career")
    val career: String,
    @SerializedName("certificate")
    val certificate: String?,
    @SerializedName("lesson")
    val lesson: String,
    @SerializedName("usercategory")
    var usercategory: String,
    @SerializedName("location")
    val location: String,

    @SerializedName("hbti")
    val hbti: List<Int>?,

    @SerializedName("matchingscore")
    var matchingscore: Int?=null,


){
    override fun toString(): String {
        return "${name} , 이력 : ${career}, 자격사항 ${certificate}, lesson : ${lesson}, category ${usercategory}, location : ${location}" +
                " ${if(matchingscore!=null) ", 매칭 점수 ${matchingscore}" else ""} \n"
    }

}