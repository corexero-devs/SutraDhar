package org.corexero.sutradhar.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.corexero.sutradhar.location.models.LiveLocation
import org.corexero.sutradhar.location.models.Location
import org.corexero.sutradhar.location.models.LocationInfoed
import org.corexero.sutradhar.location.models.TimedLocation
import kotlin.collections.zipWithNext

class LiveLocationRepository(
    private val locationRepository: LocationRepository
) {

    fun <T> getLiveLocation(locations: List<LocationInfoed<T>>): Flow<LiveLocation> {
        val segments = locations.getSegments()
        return locationRepository
            .getLiveLocationUpdates()
            .flowOn(Dispatchers.IO)
            .map { location ->
                val userSegment = findUserSegment(location, segments)
                userSegment?.let { userSegment ->
                    LiveLocation.Location<T>(
                        start = userSegment.first.start,
                        end = userSegment.first.end,
                        fraction = userSegment.second,
                        time = location.timeInMillis
                    )
                } ?: LiveLocation.NotInAnySegment
            }
    }

    private fun <T> List<LocationInfoed<T>>.getSegments(): List<Segment<T>> {
        return this.zipWithNext { a, b -> Segment(a, b) }
    }

    private fun getProjectionPercentOnSegment(
        user: TimedLocation,
        start: Location,
        end: Location
    ): Pair<Float, Float>? {

        val dx = end.lat - start.lat
        val dy = end.long - start.long
        val segmentLengthSq = dx * dx + dy * dy

        if (segmentLengthSq == 0.0) return null

        val t =
            ((user.location.lat - start.lat) * dx + (user.location.long - start.long) * dy) / segmentLengthSq
        if (t < 0.0 || t > 1.0) return null

        // Compute projected point on the segment
        val projLat = start.lat + t * dx
        val projLng = start.long + t * dy
        val projectedPoint = Location(projLat, projLng)

        val distanceToProjection = user.distanceTo(projectedPoint)
            .coerceIn(
                minimumValue = 0f,
                maximumValue = 1f
            )

        return Pair(t.toFloat(), distanceToProjection)
    }

    private fun <T> findUserSegment(
        userLocation: TimedLocation,
        segments: List<Segment<T>>
    ): Pair<Segment<T>, Float>? {
        var closestSegment: Segment<T>? = null
        var closestT = 0f
        var minDistance = Float.MAX_VALUE

        for (segment in segments) {
            val result = getProjectionPercentOnSegment(
                userLocation,
                segment.start.location,
                segment.end.location
            )
            if (result != null) {
                val (t, distance) = result
                if (distance < minDistance) {
                    minDistance = distance
                    closestSegment = segment
                    closestT = t
                }
            }
        }

        return if (closestSegment != null) closestSegment to closestT else null
    }

    private data class Segment<T>(
        val start: LocationInfoed<T>,
        val end: LocationInfoed<T>
    )

}