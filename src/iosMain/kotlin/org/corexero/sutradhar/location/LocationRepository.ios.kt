package org.corexero.sutradhar.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.corexero.sutradhar.location.models.Location
import org.corexero.sutradhar.location.models.LocationProviderStatus
import org.corexero.sutradhar.location.models.TimedLocation
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume

actual class LocationRepository {

    private val locationManager = CLLocationManager().apply {
        desiredAccuracy = kCLLocationAccuracyBest
    }

    actual fun hasLocationPermission(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    actual suspend fun getLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            if (!hasLocationPermission()) {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

                @OptIn(ExperimentalForeignApi::class)
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    val clLocation = didUpdateLocations.firstOrNull() as? CLLocation
                    val coord = clLocation?.coordinate

                    val location = coord?.useContents {
                        Location(
                            lat = latitude,
                            long = longitude
                        )
                    }
                    locationManager.delegate = null
                    continuation.resumeWith(Result.success(location))
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    locationManager.delegate = null
                    continuation.resumeWith(Result.success(null))
                }
            }

            continuation.invokeOnCancellation {
                locationManager.delegate = null
            }

            locationManager.delegate = delegate
            locationManager.requestLocation()
        }
    }

    actual fun openLocationSettings() {
        val url = NSURL(string = UIApplicationOpenSettingsURLString)
        dispatch_async(dispatch_get_main_queue()) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun isAnyProviderEnabled(): Boolean {
        return CLLocationManager.locationServicesEnabled()
    }

    actual fun getLiveLocationUpdates(interval: Long): Flow<TimedLocation> {
        if (!hasLocationPermission()) {
            return flow { }
        }

        return callbackFlow {
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

                @OptIn(ExperimentalForeignApi::class)
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    val clLocation = didUpdateLocations.firstOrNull() as? CLLocation
                    val coord = clLocation?.coordinate


                    val locationWithTimeStamp = coord?.useContents {
                        TimedLocation(
                            location = Location(
                                lat = latitude,
                                long = longitude
                            ),
                            timeInMillis = clLocation.timestamp.timeIntervalSinceReferenceDate.toLong()
                        )
                    }
                    if (locationWithTimeStamp != null) {
                        trySend(locationWithTimeStamp)
                    }
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                }
            }


            locationManager.delegate = delegate
            locationManager.requestWhenInUseAuthorization()
            locationManager.startUpdatingLocation()

            awaitClose {
                locationManager.stopUpdatingLocation()
                locationManager.delegate = null
            }
        }
    }

    actual fun getLocationAccess(): Flow<LocationProviderStatus> {
        if (!hasLocationPermission()) {
            return flow { }
        }

        return callbackFlow {
            val manager = CLLocationManager()

            fun emit() {
                val enabled = CLLocationManager.locationServicesEnabled()
                trySend(
                    if (enabled) LocationProviderStatus.ProviderEnabled
                    else LocationProviderStatus.NoProviderEnabled
                )
            }

            emit()

            // Delegate to observe auth/accuracy/service changes (iOS 14+)
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                    emit()
                }

                // For iOS < 14 (optional safety):
                override fun locationManager(
                    manager: CLLocationManager,
                    didChangeAuthorizationStatus: CLAuthorizationStatus
                ) {
                    emit()
                }
            }
            manager.delegate = delegate

            // Optional: re-check when returning from Settings
            val center = NSNotificationCenter.defaultCenter
            val observer = center.addObserverForName(
                name = UIApplicationDidBecomeActiveNotification,
                `object` = null,
                queue = NSOperationQueue.mainQueue()
            ) { _ -> emit() }

            awaitClose {
                manager.delegate = null
                center.removeObserver(observer)
            }
        }
    }

}