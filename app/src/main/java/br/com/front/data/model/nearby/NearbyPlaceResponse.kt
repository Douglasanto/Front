package br.com.front.data.model.nearby

data class NearbyPlaceResponse(
    val nome: String,
    val endereco: String?,
    val latitude: Double?,
    val longitude: Double?
)
