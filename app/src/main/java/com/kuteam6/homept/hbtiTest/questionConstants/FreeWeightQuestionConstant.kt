package com.kuteam6.homept.hbtiTest.questionConstants

import com.kuteam6.homept.hbtiTest.QuestionData

object FreeWeightQuestionConstant {

    fun getQuestionList():ArrayList<QuestionData> {
        val questionsList = ArrayList<QuestionData>()

        // 1
        val que1 = QuestionData(
            1, "헬스장 가면 파워렉에서 30분 이상 운동한다.",
        )

        questionsList.add(que1)

        // 2
        val que2 = QuestionData(
            2, "정형화된 운동보다 자유로운 운동이 좋다.",
        )

        questionsList.add(que2)


        // 3
        val que3 = QuestionData(
            3, " 맨몸 운동(팔굽혀펴기, 턱걸이, 스쿼트 등)을 한다.",
        )

        questionsList.add(que3)


        // 4
        val que4 = QuestionData(
            4, "스미스머신은 쳐다도 보지 않는다.",
        )

        questionsList.add(que4)

        return questionsList
    }
}