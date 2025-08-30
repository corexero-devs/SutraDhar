package org.corexero.sutradhar.location.models

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Location(
    val lat: Double,
    val long: Double
) {

    fun distanceTo(other: Location): Float {
        val earthRadius = 6371000.0

        val dLat = (other.lat - lat).toRadians()
        val dLon = (other.long - long).toRadians()

        val lat1 = lat.toRadians()
        val lat2 = other.lat.toRadians()

        val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (earthRadius * c).toFloat()
    }

    private fun Double.toRadians() = this * PI / 180

}