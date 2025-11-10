package com.spop.peloton.sensors.v1

import android.os.IBinder

class RpmSensor(binder: IBinder) : Sensor(Command.GetRpmRepeating, binder) {
    override fun mapValue(value: Float) = value
}

