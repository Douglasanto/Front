package br.com.front.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationHelper(private val context: Context) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { cont ->
        if (!hasLocationPermission()) {
            cont.resume(null); return@suspendCancellableCoroutine
        }
        client.lastLocation
            .addOnSuccessListener { loc -> cont.resume(loc?.let { it.latitude to it.longitude }) }
            .addOnFailureListener { cont.resume(null) }
    }
}
