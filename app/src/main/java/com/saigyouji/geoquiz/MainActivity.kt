package com.saigyouji.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton:Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var quizViewModel: QuizViewModel

    private val requestDataLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                quizViewModel.currentCheatState =
                    result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val provider = ViewModelProvider(this)
        quizViewModel = provider.get(QuizViewModel::class.java)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        Log.d(TAG, "onCreate: Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
            quizViewModel.setAnswerState(false)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
            quizViewModel.setAnswerState(false)
          }
        nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }
        prevButton.setOnClickListener{
            quizViewModel.moveToPrev()
            updateQuestion()
         }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            val option = ActivityOptionsCompat.makeClipRevealAnimation(it, 0, 0, it.width, it.height)

            requestDataLauncher.launch(intent, option)
        }
        updateQuestion()
    }

    override fun onResume() {
        super.onResume()
        if(quizViewModel.cheatCount >= 3) {
            cheatButton.isClickable = false
            cheatButton.visibility = View.GONE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: ")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion()
    {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean)
    {
        val correctAnswer   = quizViewModel.currentQuestionAnswer
        val messageResId = when{
            quizViewModel.currentCheatState -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}