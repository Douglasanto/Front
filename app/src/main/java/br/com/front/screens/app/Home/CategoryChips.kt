package br.com.front.screens.app.Home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun CategoryChips() {
    val categories = listOf("Hotéis", "Restaurantes", "Bares")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { /* TODO */ },
                selected = false,
                shape = RoundedCornerShape(24.dp),
                label = { Text(category) },
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null)
                }
            )
        }
    }
}
