package org.corexero.sutradhar.location.models

sealed interface LiveLocation {

    data class Location<T>(
        val start: LocationInfoed<T>,
        val end: LocationInfoed<T>,
        val fraction: Float,
        val time: Long
    ) : LiveLocation

    data object NotInAnySegment : LiveLocation

    data object Initializing : LiveLocation

}