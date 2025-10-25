package br.com.front.screens.app.avaliacoes.model

data class Evaluation(
    val userId: Long,
    val placeId: String,
    val parking: Int,
    val mainEntrance: Int,
    val internalCirculation: Int,
    val bathrooms: Int,
    val service: Int,
    val timestamp: Long
)
