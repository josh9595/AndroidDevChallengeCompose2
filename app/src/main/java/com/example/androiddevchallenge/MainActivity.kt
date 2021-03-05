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
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        var cacheMinute by remember { mutableStateOf(0) }
                        var cacheOneSecond by remember { mutableStateOf(0) }
                        var cacheTwoSecond by remember { mutableStateOf(0) }

                        val alphaMinute by animateFloatAsState(
                            if (cacheMinute == timerMinute) 1f else 0.4f,
                            finishedListener = {
                                cacheMinute = timerMinute
                            }
                        )
                        val alphaOneSecond by animateFloatAsState(
                            if (cacheOneSecond == timerOneSecond) 1f else 0.4f,
                            finishedListener = {
                                cacheOneSecond = timerOneSecond
                            }
                        )

                        val alphaTwoSecond by animateFloatAsState(
                            if (cacheTwoSecond == timerTwoSecond) 1f else 0.4f,
                            finishedListener = {
                                cacheTwoSecond = timerTwoSecond
                            }
                        )

                        Column {
                            IconButton(
                                onClick = { countdownViewModel.minuteUp() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Plus 1 minute"
                                    )
                                }
                            }

                            Text(
                                text = "$timerMinute",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(alphaMinute),
                                style = MaterialTheme.typography.h1
                            )
                            IconButton(
                                onClick = { countdownViewModel.minuteDown() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Minus 1 minute"
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = ":",
                                modifier = Modifier.padding(top = 40.dp),
                                style = MaterialTheme.typography.h1
                            )
                        }

                        Column {
                            IconButton(
                                onClick = { countdownViewModel.oneSecondUp() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Plus 10 seconds"
                                    )
                                }
                            }
                            Text(
                                text = "$timerOneSecond",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(alphaOneSecond),
                                style = MaterialTheme.typography.h1
                            )
                            IconButton(
                                onClick = { countdownViewModel.oneSecondDown() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Minus 10 seconds"
                                    )
                                }
                            }
                        }

                        Column {
                            IconButton(
                                onClick = { countdownViewModel.twoSecondUp() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Plus one second"
                                    )
                                }
                            }
                            Text(
                                text = "$timerTwoSecond",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(alphaTwoSecond),
                                style = MaterialTheme.typography.h1
                            )
                            IconButton(
                                onClick = { countdownViewModel.twoSecondDown() },
                                enabled = isStartButtonEnabled(countdownState),
                                modifier = Modifier.offset(x = 8.dp, y = 0.dp)
                            ) {
                                if (isStartButtonEnabled(countdownState)) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Minus 1 second"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (timerMinute != 0 || timerOneSecond != 0 || timerTwoSecond != 0) {
                                    countdownViewModel.startTimer()
                                }
                            },
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
        val topOffset = size.height * (1f - progress)

        path.reset()
        path.moveTo(0f, topOffset)
        path.lineTo(size.width, topOffset)

        drawRect(color, topLeft = Offset(0f, topOffset), alpha = 0.5f)
//        drawRect(color, topLeft = Offset(0f, topOffset), alpha = 0.3f)
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
