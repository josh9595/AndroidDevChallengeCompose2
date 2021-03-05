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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
        Bar(
            progress = calcProgress(timerMinute, timerOneSecond, timerTwoSecond)
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {

            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {

                        Column {
                            Button(onClick = { countdownViewModel.minuteUp() }, enabled = isStartButtonEnabled(countdownState)) {
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
                            Button(onClick = { countdownViewModel.minuteDown() }, enabled = isStartButtonEnabled(countdownState)) {
                                Text(
                                    text = "▼",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }

                        Column {
                            Text(
                                text = ":",
                                modifier = Modifier.padding(top = 56.dp),
                                style = MaterialTheme.typography.h1
                            )
                        }

                        Column {
                            Button(onClick = { countdownViewModel.oneSecondUp() }, enabled = isStartButtonEnabled(countdownState)) {
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
                            Button(onClick = { countdownViewModel.oneSecondDown() }, enabled = isStartButtonEnabled(countdownState)) {
                                Text(
                                    text = "▼",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }

                        Column {
                            Button(onClick = { countdownViewModel.twoSecondUp() }, enabled = isStartButtonEnabled(countdownState)) {
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
                            Button(onClick = { countdownViewModel.twoSecondDown() }, enabled = isStartButtonEnabled(countdownState)) {
                                Text(
                                    text = "▼",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = { countdownViewModel.startTimer() },
                            enabled = isStartButtonEnabled(countdownState)
                        ) {
                            Text(text = "Start")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { countdownViewModel.pauseTimer() },
                            enabled = isPauseButtonEnabled(countdownState)
                        ) {
                            Text(text = "Pause")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { countdownViewModel.stopTimer() },
                            enabled = isStopButtonEnabled(countdownState)
                        ) {
                            Text(text = "Stop")
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))

                }

            }
        }
    }
}

@Composable
fun Bar(
    color: Color = MaterialTheme.colors.primary,
    progress: Float,
) {
    val path = remember { Path() }.apply { reset() }
    Canvas(modifier = Modifier.fillMaxSize()) {
        val topOffset = (size.height + 32f) * (1f - progress)

        path.reset()
        path.moveTo(0f, topOffset)
        path.lineTo(size.width, topOffset)

        drawRect(color, topLeft = Offset(0f, topOffset), alpha = 0.5f)
        drawRect(color, topLeft = Offset(0f, topOffset), alpha = 0.3f)
        drawPath(path, color, alpha = 0.5f)
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

fun calcProgress(minute: Int, oneSecond: Int, twoSecond: Int): Float {
    return ((minute * 60).toFloat() + (oneSecond * 10).toFloat() + twoSecond.toFloat()) / 300.toFloat()
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
