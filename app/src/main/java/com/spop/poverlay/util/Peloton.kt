package com.spop.poverlay.util

import com.spop.peloton.sensors.util.IsBikePlus as LibraryIsBikePlus
import com.spop.peloton.sensors.util.IsRunningOnPeloton as LibraryIsRunningOnPeloton
import com.spop.peloton.sensors.util.calculateSpeedFromPelotonV1Power as LibraryCalculateSpeedFromPelotonV1Power

// Re-export library utilities for backward compatibility
val IsRunningOnPeloton: Boolean
    get() = LibraryIsRunningOnPeloton

val IsBikePlus: Boolean
    get() = LibraryIsBikePlus

fun calculateSpeedFromPelotonV1Power(power: Float): Float =
    LibraryCalculateSpeedFromPelotonV1Power(power)
