package org.corexero.sutradhar.location

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.corexero.sutradhar.location.models.Location
import org.corexero.sutradhar.location.models.LocationProviderStatus
import org.corexero.sutradhar.location.models.TimedLocation

actual class LocationRepository(
    private val context: Context
) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    actual fun hasLocationPermission(): Boolean {
        return (context.checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) || (context.checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    @Suppress("MissingPermission")
    actual suspend fun getLocation(): Location? {
        if (!hasLocationPermission()) return null
        return suspendCancellableCoroutine { cont ->

            val currentLocationRequest = CurrentLocationRequest
                .Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .build()

            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                currentLocationRequest,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    cont.resumeWith(
                        Result.success(
                            Location(
                                lat = location.latitude,
                                long = location.longitude
                            )
                        )
                    )
                } else {
                    cont.resumeWith(Result.success(null))
                }
            }.addOnFailureListener {
                cont.resumeWith(Result.success(null))
            }

            cont.invokeOnCancellation { cancellationTokenSource.cancel() }
        }
    }

    actual fun openLocationSettings() {
        if (!isAnyProviderEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    actual fun isAnyProviderEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGpsEnabled || isNetworkEnabled
    }

    @Suppress("MissingPermission")
    actual fun getLiveLocationUpdates(interval: Long): Flow<TimedLocation> {
        return if (hasLocationPermission()) {
            callbackFlow {

                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                var isGpsEnabled = false
                var isNetworkEnabled = false

                while (!isGpsEnabled && !isNetworkEnabled) {
                    isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    isNetworkEnabled =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (!isGpsEnabled && !isNetworkEnabled) {
                        delay(3000L)
                    }
                }

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    close()
                } else {

                    val request = LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        interval
                    ).build()

                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            super.onLocationResult(result)
                            result.locations.lastOrNull()?.let { location ->
                                trySend(
                                    TimedLocation(
                                        location = Location(
                                            lat = location.latitude,
                                            long = location.longitude
                                        ),
                                        timeInMillis = location.time
                                    )
                                )
                            }
                        }
                    }

                    fusedLocationClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        Looper.getMainLooper()
                    )

                    awaitClose {
                        fusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                }
            }
        } else {
            flow { }
        }
    }

    actual fun getLocationAccess(): Flow<LocationProviderStatus> {
        return if (hasLocationPermission()) {
            callbackFlow {

                fun emit() {
                    trySend(
                        if (isAnyProviderEnabled()) {
                            LocationProviderStatus.ProviderEnabled
                        } else {
                            LocationProviderStatus.NoProviderEnabled
                        }
                    )
                }

                emit()

                val filter = IntentFilter().apply {
                    addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
                    addAction(LocationManager.MODE_CHANGED_ACTION)
                }

                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(
                        c: Context,
                        i: Intent?
                    ) = emit()
                }

                if (Build.VERSION.SDK_INT >= 33) {
                    context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
                } else {
                    context.registerReceiver(receiver, filter)
                }

                awaitClose { context.unregisterReceiver(receiver) }
            }
        } else {
            flow { }
        }
    }

}