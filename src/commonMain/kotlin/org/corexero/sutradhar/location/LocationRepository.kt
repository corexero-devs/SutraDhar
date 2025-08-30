package org.corexero.sutradhar.location

import kotlinx.coroutines.flow.Flow
import org.corexero.sutradhar.location.models.Location
import org.corexero.sutradhar.location.models.LocationProviderStatus
import org.corexero.sutradhar.location.models.TimedLocation

private const val DEFAULT_INTERVAL = 2500L

expect class LocationRepository {

    fun hasLocationPermission(): Boolean

    suspend fun getLocation(): Location?

    fun openLocationSettings()

    fun isAnyProviderEnabled(): Boolean

    fun getLiveLocationUpdates(interval: Long = DEFAULT_INTERVAL): Flow<TimedLocation>

    fun getLocationAccess(): Flow<LocationProviderStatus>

}
