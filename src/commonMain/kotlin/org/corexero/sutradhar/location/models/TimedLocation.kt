package org.corexero.sutradhar.location.models

data class TimedLocation(
    val location: Location,
    val timeInMillis: Long
) {

    fun distanceTo(other: Location): Float = location.distanceTo(other)

}