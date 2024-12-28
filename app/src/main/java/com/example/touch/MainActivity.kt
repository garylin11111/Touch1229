package com.example.touch

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PointMode.Companion.Points
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.touch.ui.theme.ColorBlue
import com.example.touch.ui.theme.ColorGreen
import com.example.touch.ui.theme.ColorIndigo
import com.example.touch.ui.theme.ColorOrange
import com.example.touch.ui.theme.ColorPurple
import com.example.touch.ui.theme.ColorRed
import com.example.touch.ui.theme.ColorYellow
import com.example.touch.ui.theme.Points
import com.example.touch.ui.theme.TouchTheme
import kotlin.io.path.Path

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting("多指觸控Compose實例")
                    DrawCombined()
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    Row {
    Text(text = "多指觸控Compose實例",
        fontFamily = FontFamily(Font(R.font.finger)),
        fontSize = 25.sp,
        color = Color.Blue)
    Image(
        painter = painterResource(id = R.drawable.hand),
        contentDescription = "手掌",
        alpha = 0.7f,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Blue)
    )
        }
        Text(text = "作者：林政彥",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 25.sp,
            color = Color.Black)
    }
}@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawCombined() {
    val X = remember { mutableStateListOf<Float>() }
    val Y = remember { mutableStateListOf<Float>() }
    val paths = remember { mutableStateListOf<Points>() }
    var Fingers by remember { mutableStateOf(0) }

    var PaintColor: Color
    val colors = arrayListOf(
        ColorRed, ColorOrange, ColorYellow, ColorGreen,
        ColorBlue, ColorIndigo, ColorPurple
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths += Points(event.x, event.y)
                        true
                    }
                    MotionEvent.ACTION_POINTER_DOWN,
                    MotionEvent.ACTION_POINTER_UP,
                    MotionEvent.ACTION_UP -> {
                        Fingers = event.pointerCount
                        X.clear()
                        Y.clear()
                        for (i in 0 until Fingers) {
                            X.add(event.getX(i))
                            Y.add(event.getY(i))
                        }
                        true
                    }
                    else -> false
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 0 until Fingers) {
                PaintColor = colors[i % 7]
                drawCircle(PaintColor, 100f, Offset(X[i], Y[i]))
            }

            val path = androidx.compose.ui.graphics.Path()
            paths.forEachIndexed { index, point ->
                if (index == 0) {
                    path.moveTo(point.x, point.y)
                } else {
                    path.lineTo(point.x, point.y)
                }
            }
            drawPath(
                path,
                color = Color.Black,
                style = Stroke(width = 30f, join = StrokeJoin.Round)
            )
        }
    }
}