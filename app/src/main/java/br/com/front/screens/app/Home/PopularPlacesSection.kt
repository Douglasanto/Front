package br.com.front.screens.app.Home

import PlaceCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.front.R

data class PlaceData(
    val name: String,
    val category: String,
    val accessible: Boolean,
    val imageRes: Int
)


@Composable
fun PopularPlacesSection() {

    val places = listOf(
        PlaceData("Horto Florestal", "Parque ecológico", true, R.drawable.horto),
        PlaceData("Restaurante Central", "Gastronomia", false, R.drawable.cds),
        PlaceData("Bar do Zé", "Bar & Lazer", true, android.R.drawable.ic_menu_camera),
        PlaceData("Museu Histórico", "Cultura", false, android.R.drawable.ic_menu_agenda)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Locais populares",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = "ver todos",
            color = Color(0xFF2D6CDF),
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* TODO */ }
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(places) { place ->
            PlaceCard(
                name = place.name,
                category = place.category,
                accessible = place.accessible,
                imageRes = place.imageRes
            )
        }
    }

}
