package com.spop.peloton.sensors.v1

import android.os.IBinder

class ResistanceSensor(binder: IBinder) : Sensor(Command.GetResistanceRepeating, binder) {
    override fun mapValue(value: Float) = value
}

