package com.example.sensorkit.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@SuppressLint("MissingPermission")
@Composable
fun rememberAccelerometerValues(): FloatArray {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val values = remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    val isListening = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) values.value = it.values.clone() }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    LaunchedEffect(Unit) {
        delay(5000)
        isListening.value = false
        sensorManager.unregisterListener(null, accelerometer)
    }
    return values.value
}