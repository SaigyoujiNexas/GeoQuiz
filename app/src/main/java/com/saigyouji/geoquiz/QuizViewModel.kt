package com.saigyouji.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel: ViewModel() {
    var num = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_america, true),
        Question(R.string.question_asia, true))
    private val cheatState = BooleanArray(questionBank.size)


    var currentIndex = 0
    var cheatCount = 0

    var currentCheatState: Boolean
        get() = cheatState[currentIndex]
        set(value) {
            if(value && (!cheatState[currentIndex])) cheatCount++
            cheatState[currentIndex] = value}

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val length : Int
        get() = questionBank.size

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev()
    {
        currentIndex = (questionBank.size + currentIndex - 1) % questionBank.size
    }

    fun checkGrade(): Boolean = (num == questionBank.size)

    fun setAnswerState(state: Boolean = questionBank[currentIndex].state)
    {
        questionBank[currentIndex].state = state
    }
    fun getAnswerState(): Boolean = questionBank[currentIndex].state

}