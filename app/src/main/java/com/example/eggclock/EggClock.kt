package com.example.eggclock

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class EggClock : AppCompatActivity(), EggTimerView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg_clock)
    }

    fun displayTimeFromMillis(millis: Long) {
        val txt = findViewById<TextView>(R.id.timer_text)

        txt.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            Date(
                millis
            )
        )
    }

    // when the user presses the start button you should not be able to switch to another time
    private fun disableEggButtons() {
        val soft = findViewById<ImageButton>(R.id.soft_egg_button)
        val medium = findViewById<ImageButton>(R.id.medium_egg_button)
        val hard = findViewById<ImageButton>(R.id.hard_egg_button)
        soft.setColorFilter(R.color.grey)
        medium.setColorFilter(R.color.grey)
        hard.setColorFilter(R.color.grey)
        soft.isEnabled = false
        medium.isEnabled = false
        hard.isEnabled = false
        findViewById<Button>(R.id.starting_button).text = getString(R.string.stop_text)
    }

    // when the user presses the stop button, or the timer has finished
    // the egg buttons should be enabled again
    private fun enableEggButtons() {
        val soft = findViewById<ImageButton>(R.id.soft_egg_button)
        val medium = findViewById<ImageButton>(R.id.medium_egg_button)
        val hard = findViewById<ImageButton>(R.id.hard_egg_button)
        soft.clearColorFilter()
        medium.clearColorFilter()
        hard.clearColorFilter()
        soft.isEnabled = true
        medium.isEnabled = true
        hard.isEnabled = true
        findViewById<Button>(R.id.starting_button).text = getString(R.string.start_text)
    }

    var myTimer: EggTimer? = null

    private fun setTimer(timer_minutes: Int) {
        val timeInMillis = timer_minutes.toLong() * 60 * 1000
        displayTimeFromMillis(timeInMillis)
        myTimer = EggTimer(timer_minutes)
        myTimer?.addListener(EggTimerPresenter(this))
        // enabling start button because user has chosen a time
        findViewById<Button>(R.id.starting_button).isEnabled = true
    }

    // 10 minutes to hard boil an egg, 7 minutes for smiling eggs and 5 minutes for soft boiling eggs
    private var hardBoiledMinutes = 10
    private var smilingMinutes = 7
    private var softBoiledMinutes = 5


    fun softBoiledClick(view: View) {
        setTimer(softBoiledMinutes)
    }

    fun smilingClick(view: View) {
        setTimer(smilingMinutes)
    }

    fun hardBoiledClick(view: View) {
        setTimer(hardBoiledMinutes)
    }

    fun startClock(view: View) {
        if (myTimer?.isRunning() == true) {
            myTimer?.stopTimer()
            enableEggButtons()
        } else {
            myTimer?.startTimer()
            disableEggButtons()
        }
    }

    override fun onCountDown(time: Long) {
        runOnUiThread {
            displayTimeFromMillis(time)
        }
    }

    override fun onEggTimerStopped() {
        runOnUiThread {
            findViewById<TextView>(R.id.timer_text).text = getString(R.string.finish_text)
            enableEggButtons()
        }
    }
}