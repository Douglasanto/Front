package br.com.front.screens.app.avaliacoes

import CustomScaffold
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.front.navigation.Screens
import br.com.front.screens.app.avaliacoes.model.Place
import br.com.front.screens.app.avaliacoes.model.samplePlaces

@Composable
fun AvaliacaoScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    CustomScaffold(
        navController = navController,
        modifier = modifier
    ) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(Color(0xFF2D6CDF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Avaliações", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            items(samplePlaces) { place ->
                PlaceCardAvaliation(place) {
                    navController.navigate(Screens.DetalhesLocal.createRoute(place.id))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PlaceCardAvaliation(
    place: Place,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = place.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = place.category,
                color = Color.Gray,
                fontSize = 14.sp
            )
            if (place.accessible) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Acessível",
                        tint = Color.Green
                    )
                    Text("Acessível", color = Color.Green)
                }
            }
        }
    }
}

@Preview
@Composable
fun AvaliacaoScreenPreview() {
    AvaliacaoScreen(navController = NavHostController(LocalContext.current))
}