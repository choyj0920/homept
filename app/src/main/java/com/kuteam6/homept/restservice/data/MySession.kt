package com.kuteam6.homept.restservice.data

class MySession(
    var sid: Int,
    var myTraineeProfile: MyTraineeProfile?=null,
    var myTrainerProfile: MyTrainerProfile?=null,
    var sessionNow:Int
){
    override fun toString(): String {
        return "SID:  ${sid}, [${if(sessionNow==0) "트레이니 신청상태" else "트레이너 승인상태"}] \n" +
                "대상 ${if(myTraineeProfile==null) myTrainerProfile.toString() else myTraineeProfile.toString()} "
    }
}
