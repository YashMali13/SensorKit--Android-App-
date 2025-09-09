package com.example.sensorkit.ui.screens
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensorkit.R
import com.example.sensorkit.sensors.*
import com.example.sensorkit.utils.magnitude

@Composable
fun SensorScreen() {
    val scrollState = rememberScrollState()
    var showBox by remember { mutableStateOf(false) }
    var selectedSensor by remember { mutableStateOf<SensorType?>(null) }

    val accelValues = rememberAccelerometerValues()
    val gyroValues = rememberGyroscopeValues()
    val magnetoValues = rememberMagnetometerValues()
    val proxiValue = rememberProximityValue()
    val ambientValue = rememberAmbientLightValue()
    val gravityValues = rememberGravityValues()
    val stepCount = rememberStepCount()
    val stepDetect = rememberStepDetector()
    val linearAccel = rememberLinearAccelerationValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // Cyan
                        Color(0xFFFFFFFF)  // Blue
                    )
                )
            )
            .padding(vertical = 16.dp),
    ) {
        @Composable
        fun CardBox(
            colors: List<Color>,
            iconRes: Int,
            title: String,
            type: SensorType
        ) {
            var isPressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(if (isPressed) 0.95f else 1f)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(colors))
                    .scale(scale)
                    .clickable {
                        isPressed = true
                        selectedSensor = type
                        showBox = true
                        isPressed = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = title,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Define sensor rows with explicit SensorType mapping
        val sensorRows = listOf(
            listOf(
                Triple(listOf(Color(0xFF42A5F5), Color(0xFF1E88E5)), R.drawable.img, SensorType.ACCELEROMETER),
                Triple(listOf(Color(0xFFFFF176), Color(0xFFFFEE58)), R.drawable.img_1, SensorType.GYROSCOPE)
            ),
            listOf(
                Triple(listOf(Color(0xFFFF8A65), Color(0xFFD84315)), R.drawable.img_2, SensorType.MAGNETOMETER),
                Triple(listOf(Color(0xFF26A69A), Color(0xFF00796B)), R.drawable.img_3, SensorType.PROXIMITY)
            ),
            listOf(
                Triple(listOf(Color(0xFFFFEE58), Color(0xFFFDD835)), R.drawable.img_4, SensorType.LIGHT),
                Triple(listOf(Color(0xFFAB47BC), Color(0xFF7B1FA2)), R.drawable.img_5, SensorType.GRAVITY)
            ),
            listOf(
                Triple(listOf(Color(0xFF29B6F6), Color(0xFF0288D1)), R.drawable.img_6, SensorType.STEPCOUNTER),
                Triple(listOf(Color(0xFFF06292), Color(0xFFC2185B)), R.drawable.img_7, SensorType.STEPDETECTOR)
            ),
            listOf(
                Triple(listOf(Color(0xFF66BB6A), Color(0xFF2E7D32)), R.drawable.img_8, SensorType.LINEAR),
                Triple(listOf(Color(0xFFFFB74D), Color(0xFFF57C00)), R.drawable.img_9, SensorType.GPS)
            )
        )

        // Render rows
        sensorRows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { (colors, icon, type) ->
                    CardBox(colors, icon, type.name.replace("_", " "), type)
                }
            }
        }
    }

    // Slide-up box with sensor data
    SlideUpBoxWithAnimation(visible = showBox, onDismiss = { showBox = false }) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            selectedSensor?.let { sensor ->
                Text(
                    text = "${sensor.name.replace("_", " ")} Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                when (sensor) {
                    SensorType.ACCELEROMETER -> {
                        Text("X = %.2f".format(accelValues[0]))
                        Text("Y = %.2f".format(accelValues[1]))
                        Text("Z = %.2f".format(accelValues[2]))
                    }
                    SensorType.GYROSCOPE -> {
                        Text("X = %.2f".format(gyroValues[0]))
                        Text("Y = %.2f".format(gyroValues[1]))
                        Text("Z = %.2f".format(gyroValues[2]))
                    }
                    SensorType.MAGNETOMETER -> {
                        Text("X = %.2f".format(magnetoValues[0]))
                        Text("Y = %.2f".format(magnetoValues[1]))
                        Text("Z = %.2f".format(magnetoValues[2]))
                    }
                    SensorType.PROXIMITY -> {
                        Text(if (proxiValue < 5f) "Device is too close" else "Device is far enough")
                    }
                    SensorType.LIGHT -> {
                        Text("Illuminance = %.2f lx".format(ambientValue))
                    }
                    SensorType.GRAVITY -> {
                        Text("X = %.2f".format(gravityValues[0]))
                        Text("Y = %.2f".format(gravityValues[1]))
                        Text("Z = %.2f".format(gravityValues[2]))
                        Text("Total = %.2f m/s²".format(magnitude(gravityValues[0], gravityValues[1], gravityValues[2])))
                    }
                    SensorType.STEPCOUNTER -> Text("Steps Taken = $stepCount")
                    SensorType.STEPDETECTOR -> Text("Steps Detected = $stepDetect")
                    SensorType.LINEAR -> Text("Total Acceleration = %.2f m/s²".format(magnitude(linearAccel[0], linearAccel[1], linearAccel[2])))
                    SensorType.GPS -> Text("COMING SOON")
                }
            }
        }
    }
}