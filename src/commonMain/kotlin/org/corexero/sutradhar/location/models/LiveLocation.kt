package org.corexero.sutradhar.location.models

sealed interface LiveLocation {

    data class Location(
        val start: LocationInfoed,
        val end: LocationInfoed,
        val fraction: Float,
        val time: Long
    ) : LiveLocation

    data object NotInAnySegment : LiveLocation

    data object Initializing : LiveLocation

}