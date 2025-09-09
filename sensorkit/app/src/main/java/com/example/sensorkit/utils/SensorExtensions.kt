
package com.example.sensorkit.utils

import kotlin.math.sqrt

fun magnitude(x: Float, y: Float, z: Float): Float =
    sqrt(x * x + y * y + z * z)
