package com.kuteam6.homept.chat
import kotlin.collections.HashMap

class ChatModel (val users: HashMap<String, Boolean> = HashMap(),
                 val comments : HashMap<String, Comment> = HashMap()){
    class Comment(val uid: String? = null, val message: String? = null, val time: String? = null, @field:JvmField var hasPT: Boolean? = false)
}
