package br.com.front.data.repository

import br.com.front.data.api.RetrofitInstance
import br.com.front.data.model.nearby.NearbyPlaceResponse
import android.util.Log

class NearbyRepository {
    private val api = RetrofitInstance.apiService

    suspend fun fetchNearby(lat: Double, lng: Double): Result<List<NearbyPlaceResponse>> = runCatching {
        val restaurants = api.getNearbyPlaces(lat, lng, "restaurant")
        val bars = api.getNearbyPlaces(lat, lng, "bar")
        val list = mutableListOf<NearbyPlaceResponse>()
        if (restaurants.isSuccessful) {
            val body = restaurants.body()
            Log.d("NEARBY", "restaurants ok lat=$lat lng=$lng size=${body?.size ?: 0}")
            body?.let { list.addAll(it) }
        } else {
            Log.d("NEARBY", "restaurants fail code=${restaurants.code()} msg=${restaurants.message()}")
        }
        if (bars.isSuccessful) {
            val body = bars.body()
            Log.d("NEARBY", "bars ok lat=$lat lng=$lng size=${body?.size ?: 0}")
            body?.let { list.addAll(it) }
        } else {
            Log.d("NEARBY", "bars fail code=${bars.code()} msg=${bars.message()}")
        }
        list
    }
}
