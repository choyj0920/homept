package com.kuteam6.homept.hbtiTest.questionConstants

import com.kuteam6.homept.hbtiTest.QuestionData

object HeavyQuestionConstant {

    fun getQuestionList():ArrayList<QuestionData> {
        val questionsList = ArrayList<QuestionData>()

        // 1
        val que1 = QuestionData(
            1, "세트 당 반복 횟수 (1개, 3개, 5개, 8개, 10개 이상)",
        )

        questionsList.add(que1)

        // 2
        val que2 = QuestionData(
            2, "고강도 인터벌 운동을 한다.",
        )

        questionsList.add(que2)


        // 3
        val que3 = QuestionData(
            3, "빠른 시간 내에 효과를 보기를 원한다.",
        )

        questionsList.add(que3)


        // 4
        val que4 = QuestionData(
            4, "3대 운동을 좋아한다.",
        )

        questionsList.add(que4)

        return questionsList
    }
}