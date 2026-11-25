package com.emtrix.spendex.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(.88f) }
    val offset = remember { Animatable(12f) }

    // Capture theme colors outside Canvas
    val accentColor = MaterialTheme.colorScheme.primary
    val subtitleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f)

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                1f,
                tween(520, easing = FastOutSlowInEasing)
            )
        }

        launch {
            scale.animateTo(
                1f,
                tween(650, easing = FastOutSlowInEasing)
            )
        }

        launch {
            offset.animateTo(
                0f,
                tween(650, easing = FastOutSlowInEasing)
            )
        }

        delay(980)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
                .offset(y = offset.value.dp)
        ) {

            Canvas(
                modifier = Modifier.size(54.dp)
            ) {
                drawCircle(
                    color = accentColor,
                    radius = size.minDimension * .43f,
                    style = Stroke(width = 4.dp.toPx())
                )

                drawLine(
                    color = accentColor,
                    start = Offset(
                        size.width * .27f,
                        size.height * .55f
                    ),
                    end = Offset(
                        size.width * .46f,
                        size.height * .72f
                    ),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = accentColor,
                    start = Offset(
                        size.width * .46f,
                        size.height * .72f
                    ),
                    end = Offset(
                        size.width * .76f,
                        size.height * .30f
                    ),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "SpendeX",
                style = MaterialTheme.typography.displaySmall
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "by Emtrix",
                color = subtitleColor,
                fontSize = 13.sp
            )
        }
    }
}
