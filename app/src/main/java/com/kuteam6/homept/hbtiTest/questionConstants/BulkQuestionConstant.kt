package com.kuteam6.homept.hbtiTest.questionConstants

import com.kuteam6.homept.hbtiTest.QuestionData

object BulkQuestionConstant {
    fun getQuestionList():ArrayList<QuestionData> {
        val questionsList = ArrayList<QuestionData>()

        // 1
        val que1 = QuestionData(
            1, "체중이 늘면 기분이 좋다.",
        )

        questionsList.add(que1)

        // 2
        val que2 = QuestionData(
            2, "근손실보다 차라리 체중이 느는 게 낫다.",
        )

        questionsList.add(que2)


        // 3
        val que3 = QuestionData(
            3, "유산소 운동보다 웨이트 운동을 선호하는 편이다.",
        )

        questionsList.add(que3)


        // 4
        val que4 = QuestionData(
            4, "주변으로부터 근돼(근육 돼지)라는 말을 들어봤다.",
        )

        questionsList.add(que4)

        return questionsList
    }
}