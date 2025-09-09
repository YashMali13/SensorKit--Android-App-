package com.example.sensorkit.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

@Composable
fun SlideUpBoxWithAnimation(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    boxHeight: Dp = 220.dp, // shorter height
    content: @Composable ColumnScope.() -> Unit
) {
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else boxHeight + 50.dp,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "slideUp"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "fadeIn"
    )

    val contentOffsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 10.dp,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "slideContent"
    )

    if (visible || offsetY < boxHeight + 50.dp) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = if (visible) 0.4f else 0f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = modifier
                    .offset(y = offsetY)
                    .fillMaxWidth()
                    .height(boxHeight)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .shadow(8.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFB2EBF2),
                                    Color(0xFF80DEEA)
                                )
                            ),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    // Grab bar
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Gray.copy(alpha = 0.6f))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Animated content
                    Column(
                        modifier = Modifier
                            .offset(y = contentOffsetY)
                            .alpha(contentAlpha)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}