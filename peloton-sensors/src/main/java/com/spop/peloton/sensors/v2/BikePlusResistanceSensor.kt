package com.spop.peloton.sensors.v2

import android.os.IBinder

class BikePlusResistanceSensor(binder: IBinder) : BikePlusSensor(Command.GetResistanceRepeating, binder) {
    override fun mapValue(value: Float) = value
}

