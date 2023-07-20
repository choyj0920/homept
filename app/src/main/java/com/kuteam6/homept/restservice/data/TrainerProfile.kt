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
    val gender: String,

    @SerializedName("career")
    val career: String,
    @SerializedName("certificate")
    val certificate: String?,
    @SerializedName("lesson")
    val lesson: String,
    @SerializedName("usercategory")
    val usercategory: String,
    @SerializedName("location")
    val location: String,
){
    override fun toString(): String {
        return "${name} , 이력 : ${career}, 자격사항 ${certificate}, lesson : ${lesson}, category ${usercategory}, location : ${location}\n"
    }
}

