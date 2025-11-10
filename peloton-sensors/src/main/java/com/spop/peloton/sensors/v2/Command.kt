package com.spop.peloton.sensors.v2

enum class Command(val id: Int) {
    GetRpmRepeating(1),
    GetPowerRepeating(2),
    GetResistanceRepeating(3)
}

