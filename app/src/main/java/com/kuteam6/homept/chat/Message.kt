package com.kuteam6.homept.chat

data class Message(
    var message : String?,
    // 접속자 uid
    var sendId : String?
){
    constructor():this("","")
}
