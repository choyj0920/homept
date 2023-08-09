package com.kuteam6.homept.hbtiTest.questionConstants

import com.kuteam6.homept.hbtiTest.QuestionData

object EnjoyQuestionConstant {
    fun getQuestionList():ArrayList<QuestionData> {
        val questionsList = ArrayList<QuestionData>()

        // 1
        val que1 = QuestionData(
            1, "운동이 오마카세보다 맛있다.",
        )

        questionsList.add(que1)

        // 2
        val que2 = QuestionData(
            2, "억압된 분위기를 싫어한다.",
        )

        questionsList.add(que2)


        // 3
        val que3 = QuestionData(
            3, "운동은 즐겁게 해야한다고 생각한다",
        )

        questionsList.add(que3)


        // 4
        val que4 = QuestionData(
            4, "근육통에 희열을 느낀다.",
        )

        questionsList.add(que4)

        return questionsList
    }
}