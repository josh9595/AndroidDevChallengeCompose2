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

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(countdownViewModel: CountdownViewModel = CountdownViewModel()) {
    val countdownState: CountdownState by countdownViewModel.countdownState.observeAsState(CountdownState.IDLE)
    val timerMinute: Int by countdownViewModel.timerMinute.observeAsState(0)
    val timerOneSecond: Int by countdownViewModel.timerOneSecond.observeAsState(0)
    val timerTwoSecond: Int by countdownViewModel.timerTwoSecond.observeAsState(0)
    val mainHandler = Handler(Looper.getMainLooper())

    mainHandler.post(object : Runnable {
        override fun run() {
            if (countdownState == CountdownState.COUNTING) countdownViewModel.secondDown()
            mainHandler.postDelayed(this, 1000)
        }
    })

    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()) {
                Column() {
                    Button(onClick = { countdownViewModel.minuteUp() }) {
                        Text(
                            text = "▲",
                            style = MaterialTheme.typography.h4
                        )
                    }
                    Text(
                        text = "$timerMinute",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        style = MaterialTheme.typography.h1
                    )
                    Button(onClick = { countdownViewModel.minuteDown() }) {
                        Text(
                            text = "▼",
                            style = MaterialTheme.typography.h4
                        )
                    }
                }

                Column() {
                    Text(
                        text = ":",
                        modifier = Modifier.padding(top = 56.dp),
                        style = MaterialTheme.typography.h1
                    )
                }

                Column() {
                    Button(onClick = { countdownViewModel.oneSecondUp() }) {
                        Text(
                            text = "▲",
                            style = MaterialTheme.typography.h4
                        )
                    }
                    Text(
                        text = "$timerOneSecond",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        style = MaterialTheme.typography.h1
                    )
                    Button(onClick = { countdownViewModel.oneSecondDown() }) {
                        Text(
                            text = "▼",
                            style = MaterialTheme.typography.h4
                        )
                    }
                }

                Column() {
                    Button(onClick = { countdownViewModel.twoSecondUp() }) {
                        Text(
                            text = "▲",
                            style = MaterialTheme.typography.h4
                        )
                    }
                    Text(
                        text = "$timerTwoSecond",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        style = MaterialTheme.typography.h1
                    )
                    Button(onClick = { countdownViewModel.twoSecondDown() }) {
                        Text(
                            text = "▼",
                            style = MaterialTheme.typography.h4
                        )
                    }
                }
            }

            Text(
                text = "State - $countdownState",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )

            Row() {
                Button(
                    onClick = { countdownViewModel.startTimer() },
                    enabled = isStartButtonEnabled(countdownState)
                ) {
                    Text(text = "Start")
                }
                Button(
                    onClick = { countdownViewModel.pauseTimer() },
                    enabled = isPauseButtonEnabled(countdownState)
                ) {
                    Text(text = "Pause")
                }
                Button(
                    onClick = { countdownViewModel.stopTimer() },
                    enabled = isStopButtonEnabled(countdownState)
                ) {
                    Text(text = "Stop")
                }
            }
        }
    }
}

fun isStartButtonEnabled(countdownState: CountdownState): Boolean {
    return countdownState != CountdownState.COUNTING
}

fun isPauseButtonEnabled(countdownState: CountdownState): Boolean {
    return countdownState == CountdownState.COUNTING
}

fun isStopButtonEnabled(countdownState: CountdownState): Boolean {
    return countdownState != CountdownState.IDLE
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
