package br.com.front.screens.app.avaliacoes.model

data class Place(
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val openingHours: String,
    val description: String,
    val accessible: Boolean
)

val samplePlaces = listOf(
    Place(
        id = "1",
        name = "Horto Florestal",
        category = "Parque ecológico",
        address = "Av. Comogeri – Centro",
        openingHours = "Todos os dias de 09h às 17h",
        description = "Produção de mudas de árvores para áreas urbanas...",
        accessible = true
    ),
)