package com.example.eggclock

interface EggTimerView {
    fun onCountDown(time: Long)
    fun onEggTimerStopped()
}

class EggTimerPresenter(val view: EggTimerView) : EggTimer.TimerObserver {
    override fun timerTick(millisLeft: Long) {
        view.onCountDown(millisLeft)
    }

    override fun timerFinished() {
        view.onEggTimerStopped()
    }
}