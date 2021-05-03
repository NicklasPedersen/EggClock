package com.example.eggclock

import android.os.CountDownTimer


private const val millisInMinutes: Long = 60 * 1000
private const val intervalThresholdMillis: Long = 1000

class EggTimer(minutes: Int) {
    var countDownTimer: CountDownTimer =
        object: CountDownTimer(minutes * millisInMinutes, intervalThresholdMillis) {
            override fun onFinish() {
                for (observer in observers) {
                    observer.timerFinished()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                for (observer in observers) {
                    observer.timerTick(millisUntilFinished)
                }
            }
        }

    interface TimerObserver {
        fun timerTick(millisLeft: Long)
        fun timerFinished()
    }

    // the state machine is thus:
    // NOT_STARTED -> RUNNING by calling startTimer()
    // PAUSED -> RUNNING by calling startTimer()
    // RUNNING -> PAUSED by calling stopTimer()
    // RUNNING -> FINISHED when the time runs out

    enum class TimerState {
        NOT_STARTED,
        RUNNING,
        PAUSED,
        FINISHED,
    }

    private val observers = ArrayList<TimerObserver>()
    var state: TimerState = TimerState.NOT_STARTED

    fun addListener(observer: TimerObserver) {
        observers.add(observer)
    }

    fun removeListener(observer: TimerObserver) {
        observers.remove(observer)
    }

    fun startTimer() {
        // if the time has run out we are finished
        if (state == TimerState.FINISHED) {
            return
        }
        if (state == TimerState.PAUSED) {
            countDownTimer = object: CountDownTimer(remainingTime, intervalThresholdMillis) {
                override fun onFinish() {
                    for (observer in observers) {
                        observer.timerFinished()
                    }
                    state = TimerState.FINISHED
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    for (observer in observers) {
                        observer.timerTick(millisUntilFinished)
                    }
                }
            }
        }
        countDownTimer.start()
        state = TimerState.RUNNING
    }

    var remainingTime: Long = 0

    fun stopTimer() {
        if (state != TimerState.RUNNING) {
            return
        }
        state = TimerState.PAUSED
        countDownTimer.cancel()
    }

    fun isRunning(): Boolean {
        return state == TimerState.RUNNING
    }

    fun isFinished(): Boolean {
        return state == TimerState.FINISHED
    }
}