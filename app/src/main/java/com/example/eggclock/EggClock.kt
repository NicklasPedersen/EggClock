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

class EggClock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg_clock)
    }

    private fun enableStartButton() {
        findViewById<Button>(R.id.starting_button).isEnabled = true
    }

    fun displayTimeFromMillis(millis: Long) {
        var txt = findViewById<TextView>(R.id.timer_text)

        txt.text = SimpleDateFormat("mm:ss").format(
            Date(
                millis
            )
        )
    }

    private fun disableEggButtons() {
        var soft = findViewById<ImageButton>(R.id.soft_egg_button)
        var medium = findViewById<ImageButton>(R.id.medium_egg_button)
        var hard = findViewById<ImageButton>(R.id.hard_egg_button)
        soft.setColorFilter(R.color.grey)
        medium.setColorFilter(R.color.grey)
        hard.setColorFilter(R.color.grey)
        soft.isEnabled = false;
        medium.isEnabled = false;
        hard.isEnabled = false;
        findViewById<Button>(R.id.starting_button).text = "Stop"
    }
    private fun enableEggButtons() {
        var soft = findViewById<ImageButton>(R.id.soft_egg_button)
        var medium = findViewById<ImageButton>(R.id.medium_egg_button)
        var hard = findViewById<ImageButton>(R.id.hard_egg_button)
        soft.clearColorFilter()
        medium.clearColorFilter()
        hard.clearColorFilter()
        soft.isEnabled = true;
        medium.isEnabled = true;
        hard.isEnabled = true;
        findViewById<Button>(R.id.starting_button).text = "Start"
    }
    var started: Boolean = false

    var timer: CountDownTimer? = null
    private fun setTimer(timer_minutes: Int) {
        val timeInMillis = timer_minutes.toLong() * 60 * 1000
        displayTimeFromMillis(timeInMillis)
        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
        }, 3000)
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                runOnUiThread {
                    displayTimeFromMillis(millisUntilFinished)
                }
            }

            override fun onFinish() {
                runOnUiThread {
                    findViewById<TextView>(R.id.timer_text).text = "done!"
                }
                enableEggButtons()
            }
        }
        enableStartButton()
    }

    private var hard_boiled = 10
    private var smiling = 7
    private var soft_boiled = 5


    fun softBoiledClick(view: View) {
        setTimer(soft_boiled)
    }

    fun smilingClick(view: View) {
        setTimer(smiling)
    }

    fun hardBoiledClick(view: View) {
        setTimer(hard_boiled)
    }
    fun startClock(view: View) {
        if (started) {
            timer?.cancel()
            enableEggButtons()
        } else {
            timer?.start();
            disableEggButtons()
        }
        started = !started
    }
}