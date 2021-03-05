package com.example.androiddevchallenge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountdownViewModel : ViewModel() {

    private val _countdownState = MutableLiveData(CountdownState.IDLE)
    val countdownState: LiveData<CountdownState> = _countdownState

    private val _timerMinute = MutableLiveData(0)
    val timerMinute: LiveData<Int> = _timerMinute

    private val _timerOneSecond = MutableLiveData(0)
    val timerOneSecond: LiveData<Int> = _timerOneSecond

    private val _timerTwoSecond = MutableLiveData(0)
    val timerTwoSecond: LiveData<Int> = _timerTwoSecond

    fun minuteUp() {
        _timerMinute.value?.let {
            if (it == 4) {
                _timerMinute.value = 0
            } else {
                _timerMinute.value = it + 1
            }
        }
    }

    fun minuteDown() {
        _timerMinute.value?.let {
            if (it == 0) {
                _timerMinute.value = 4
            } else {
                _timerMinute.value = it - 1
            }
        }
    }

    fun oneSecondUp() {
        _timerOneSecond.value?.let {
            if (it == 5) {
                _timerOneSecond.value = 0
            } else {
                _timerOneSecond.value = it + 1
            }
        }
    }

    fun oneSecondDown() {
        _timerOneSecond.value?.let {
            if (it == 0) {
                _timerOneSecond.value = 5
            } else {
                _timerOneSecond.value = it - 1
            }
        }
    }

    fun twoSecondUp() {
        _timerTwoSecond.value?.let {
            if (it == 9) {
                _timerTwoSecond.value = 0
            } else {
                _timerTwoSecond.value = it + 1
            }
        }
    }

    fun twoSecondDown() {
        _timerTwoSecond.value?.let {
            if (it == 0) {
                _timerTwoSecond.value = 9
            } else {
                _timerTwoSecond.value = it - 1
            }
        }
    }

    fun secondDown() {
        if (_timerTwoSecond.value != 0) {
            _timerTwoSecond.value?.let { _timerTwoSecond.value = it - 1 }
            if (
                _timerTwoSecond.value == 0 &&
                _timerOneSecond.value == 0 &&
                _timerMinute.value == 0
            ) {
                _countdownState.value = CountdownState.IDLE
            }
        } else {
            _timerTwoSecond.value?.let { _timerTwoSecond.value = 9 }
            if (_timerOneSecond.value != 0) {
                _timerOneSecond.value?.let { _timerOneSecond.value = it - 1 }
            } else {
                _timerOneSecond.value?.let { _timerOneSecond.value = 5 }
                if (_timerMinute.value != 0) {
                    _timerMinute.value?.let { _timerMinute.value = it - 1 }
                } else {
                    _countdownState.value = CountdownState.IDLE
                }
            }
        }
    }

    fun startTimer() {
        _countdownState.value = CountdownState.COUNTING
    }

    fun pauseTimer() {
        _countdownState.value = CountdownState.PAUSED
    }

    fun stopTimer() {
        _countdownState.value = CountdownState.IDLE
        _timerMinute.value?.let { _timerMinute.value = 0 }
        _timerOneSecond.value?.let { _timerOneSecond.value = 0 }
        _timerTwoSecond.value?.let { _timerTwoSecond.value = 0 }
    }

}

enum class CountdownState {
    IDLE,
    PAUSED,
    COUNTING,
}