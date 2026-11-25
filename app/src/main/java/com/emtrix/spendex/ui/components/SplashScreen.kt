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
    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(520, easing = FastOutSlowInEasing)) }
        launch { scale.animateTo(1f, tween(650, easing = FastOutSlowInEasing)) }
        launch { offset.animateTo(0f, tween(650, easing = FastOutSlowInEasing)) }
        delay(980)
        onFinished()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.alpha(alpha.value).scale(scale.value).offset(y = offset.value.dp)) {
            Canvas(Modifier.size(54.dp)) {
                val c = MaterialTheme.colorScheme.primary
                drawCircle(c, radius = size.minDimension * .43f, style = Stroke(width = 4.dp.toPx()))
                drawLine(c, Offset(size.width * .27f, size.height * .55f), Offset(size.width * .46f, size.height * .72f), 4.dp.toPx(), cap = StrokeCap.Round)
                drawLine(c, Offset(size.width * .46f, size.height * .72f), Offset(size.width * .76f, size.height * .30f), 4.dp.toPx(), cap = StrokeCap.Round)
            }
            Spacer(Modifier.height(18.dp))
            Text("SpendeX", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(4.dp))
            Text("by Emtrix", color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f), fontSize = 13.sp)
        }
    }
}
