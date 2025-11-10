package com.spop.peloton.sensors.util

import android.os.Build
import kotlin.math.pow
import kotlin.math.sqrt

private const val PelotonBrand = "Peloton"

/**
 * Checks if the device is running on a Peloton bike
 */
val IsRunningOnPeloton: Boolean
    get() = Build.BRAND == PelotonBrand

/**
 * Checks if the device is a Peloton Bike+
 * Note: This assumes the model name is the same for all Peloton Bike+ devices
 */
val IsBikePlus: Boolean
    get() = Build.MODEL == "PLTN-TTR01"

/**
 * Calculates speed from Peloton V1 power readings
 * Formula from: https://ihaque.org/posts/2020/12/25/pelomon-part-ib-computing-speed/
 */
fun calculateSpeedFromPelotonV1Power(power: Float): Float =
    if (power < 0.1f) {
        0f
    } else {
        val pwrSqrt = sqrt(power)
        if (power < 26f) {
            0.057f - (0.172f * pwrSqrt) + (0.759f * pwrSqrt.pow(2)) - (0.079f * pwrSqrt.pow(3))
        } else {
            -1.635f + (2.325f * pwrSqrt) - (0.064f * pwrSqrt.pow(2)) + (0.001f * pwrSqrt.pow(3))
        }
    }

