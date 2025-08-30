package org.corexero.sutradhar.location.models

data class LocationInfoed<T>(
    val location: Location,
    val entity: T
)