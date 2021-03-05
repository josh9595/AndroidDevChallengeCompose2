/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

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
