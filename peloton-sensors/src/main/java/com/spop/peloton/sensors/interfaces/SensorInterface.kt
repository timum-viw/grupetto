package com.spop.peloton.sensors.interfaces

import com.spop.peloton.sensors.util.calculateSpeedFromPelotonV1Power
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interface for accessing Peloton bike sensor data
 * Provides flows for power, cadence (RPM), resistance, and calculated speed
 */
interface SensorInterface {
    val power: Flow<Float>
    val cadence: Flow<Float>
    val resistance: Flow<Float>
    val speed: Flow<Float>
        get() = power.map(::calculateSpeedFromPelotonV1Power)
}

