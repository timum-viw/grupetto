# Peloton Sensors Library

This Android library provides access to Peloton bike sensor data (power, cadence, resistance) for Gen 2 Peloton Bikes and Bike+.

## Features

- Support for multiple Peloton bike models:
  - Peloton Bike (Gen 2) - V1 and V1New interfaces
  - Peloton Bike+ - V2 interface
- Real-time sensor data via Kotlin Flows
- Automatic bike model detection
- Resistance filtering to handle sensor spikes

## Usage

### Basic Usage

```kotlin
import com.spop.peloton.sensors.interfaces.PelotonBikeSensorInterfaceV1New
import com.spop.peloton.sensors.interfaces.PelotonBikePlusSensorInterface
import com.spop.peloton.sensors.util.IsRunningOnPeloton
import com.spop.peloton.sensors.util.IsBikePlus

val context: Context = // your context

val sensorInterface = if (IsRunningOnPeloton) {
    if (IsBikePlus) {
        PelotonBikePlusSensorInterface(context)
    } else {
        PelotonBikeSensorInterfaceV1New(context)
    }
} else {
    // Handle non-Peloton devices (e.g., use a dummy implementation)
    null
}

// Collect sensor data
lifecycleScope.launch {
    sensorInterface?.power?.collect { power ->
        // Handle power data (in watts)
        println("Power: $power")
    }
    
    sensorInterface?.cadence?.collect { cadence ->
        // Handle cadence data (in RPM)
        println("Cadence: $cadence")
    }
    
    sensorInterface?.resistance?.collect { resistance ->
        // Handle resistance data
        println("Resistance: $resistance")
    }
    
    sensorInterface?.speed?.collect { speed ->
        // Handle calculated speed (in m/s)
        println("Speed: $speed")
    }
}

// Don't forget to clean up
sensorInterface?.stop()
```

### Sensor Interface

The `SensorInterface` provides the following Flow properties:
- `power: Flow<Float>` - Power in watts
- `cadence: Flow<Float>` - Cadence in RPM
- `resistance: Flow<Float>` - Resistance level
- `speed: Flow<Float>` - Calculated speed in m/s (derived from power)

### Permissions

The library requires the following permissions (automatically merged from the library's manifest):
- `onepeloton.permission.ACCESS_SENSOR_SERVICE`
- `onepeloton.permission.SUBSCRIPTION_TYPE_ACCESS`

## Integration

### Gradle

Add the library module to your `settings.gradle`:

```gradle
include ':peloton-sensors'
```

Add the dependency in your app's `build.gradle`:

```gradle
dependencies {
    implementation project(':peloton-sensors')
    // ... other dependencies
}
```

### Dead Sensor Detection

The library includes a `DeadSensorDetector` class that monitors sensor activity and detects when the Peloton sensor service stops responding:

```kotlin
import com.spop.peloton.sensors.DeadSensorDetector

val detector = DeadSensorDetector(sensorInterface, coroutineContext)

lifecycleScope.launch {
    detector.deadSensorDetected.collect {
        // Handle dead sensor detection
        showError("Sensor service is not responding. Please restart your Peloton.")
    }
}
```

### Dead Sensor Interface

For testing purposes, the library provides a `DeadSensorInterface` that simulates a non-responsive sensor:

```kotlin
import com.spop.peloton.sensors.interfaces.DeadSensorInterface

val deadSensor = DeadSensorInterface()
// All sensor flows will be empty (no values emitted)
```

### Dummy Sensor Interface

For testing and development when not running on an actual Peloton bike, the library provides a `DummySensorInterface` that generates fake sensor data using sine waves:

```kotlin
import com.spop.peloton.sensors.interfaces.DummySensorInterface

val dummySensor = DummySensorInterface()
// Generates simulated power, cadence, and resistance values
// Useful for testing in emulators or on non-Peloton devices
```

## Architecture

The library is organized into the following packages:

- `com.spop.peloton.sensors.interfaces` - Main sensor interfaces and implementations
- `com.spop.peloton.sensors.v1` - V1 sensor implementation (legacy)
- `com.spop.peloton.sensors.v2` - V2 sensor implementation (Bike+)
- `com.spop.peloton.sensors.v1new` - V1New sensor implementation (callback-based)
- `com.spop.peloton.sensors.util` - Utility functions
- `com.spop.peloton.sensors` - Dead sensor detection and monitoring

## Notes

- The library automatically filters resistance values to handle occasional sensor spikes
- Sensor interfaces must be properly stopped when no longer needed to prevent resource leaks
- The library uses Timber for logging - ensure Timber is initialized in your application

## License

This library is part of the Grupetto project and follows the same license terms.

