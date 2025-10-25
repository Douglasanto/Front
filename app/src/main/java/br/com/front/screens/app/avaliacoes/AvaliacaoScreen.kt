package br.com.front.screens.app.avaliacoes

import CustomScaffold
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import br.com.front.data.session.EvaluationSession
import br.com.front.data.session.UserSession
import br.com.front.screens.app.avaliacoes.model.Evaluation
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import br.com.front.data.model.nearby.NearbyPlaceResponse
import br.com.front.data.repository.NearbyRepository
import br.com.front.data.repository.UserRepository
import br.com.front.data.session.SelectedPlaceSession
import kotlinx.coroutines.launch
import br.com.front.util.LocationHelper

@Composable
fun AvaliacaoScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var mostrarPopup by remember { mutableStateOf(false) }
    var locais by remember { mutableStateOf(listOf<NearbyPlaceResponse>()) }
    var loading by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val nearbyRepo = remember { NearbyRepository() }
    val userRepo = remember { UserRepository() }

    val currentUser = UserSession.user.collectAsState().value
    val allEvaluations = EvaluationSession.evaluations.collectAsState().value
    val userEvaluations = remember(currentUser, allEvaluations) {
        val uid = currentUser?.id
        if (uid != null) allEvaluations.filter { it.userId == uid } else emptyList()
    }

    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    CustomScaffold(
        navController = navController,
        modifier = modifier
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
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

                if (userEvaluations.isEmpty()) {
                    item {
                        Text(
                            text = "Você ainda não possui avaliações. Toque em + para adicionar.",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(userEvaluations) { evaluation ->
                        EvaluationCard(evaluation)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    if (currentUser == null) return@FloatingActionButton
                    scope.launch {
                        loading = true
                        erro = null
                        locais = emptyList()
                        try {
                            val userResp = userRepo.getUser(currentUser.id.toString())
                            Log.d("AVALIACAO", "getUser code=${userResp.code()} isSuccessful=${userResp.isSuccessful}")
                            val coords = userResp.body()?.data
                            var lat = coords?.latitude
                            var lng = coords?.longitude
                            Log.d("AVALIACAO", "coords from backend lat=$lat lng=$lng userId=${currentUser.id}")
                            if (lat == null || lng == null) {
                                val device = locationHelper.getLastLocation()
                                lat = device?.first
                                lng = device?.second
                                Log.d("AVALIACAO", "fallback device location lat=$lat lng=$lng")
                            }
                            if (userResp.isSuccessful && lat != null && lng != null) {
                                val result = nearbyRepo.fetchNearby(lat, lng)
                                result.onSuccess { 
                                    locais = it
                                    Log.d("AVALIACAO", "nearby success size=${it.size} lat=$lat lng=$lng")
                                }
                                    .onFailure { erro = "Erro ao carregar locais próximos. Tente novamente mais tarde." }
                            } else {
                                erro = "Não foi possível obter sua localização."
                            }
                        } catch (e: Exception) {
                            Log.d("AVALIACAO", "exception msg=${e.message}")
                            erro = "Erro ao carregar locais próximos. Tente novamente mais tarde."
                        } finally {
                            loading = false
                            mostrarPopup = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF2D6CDF)
            ) {
                Icon(imageVector = Icons.Default.Add, tint = Color.White, contentDescription = "Adicionar avaliação")
            }

            if (mostrarPopup) {
                AlertDialog(
                    onDismissRequest = { mostrarPopup = false },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { mostrarPopup = false }) { Text("Fechar") }
                    },
                    title = { Text("Selecione um local para avaliar") },
                    text = {
                        Column {
                            when {
                                loading -> {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Alignment.CenterHorizontally as Arrangement.Horizontal) {
                                        CircularProgressIndicator()
                                    }
                                }
                                erro != null -> {
                                    Text(erro!!, color = Color.Red)
                                }
                                locais.isEmpty() -> {
                                    Text("Nenhum restaurante ou bar encontrado próximo à sua localização.")
                                }
                                else -> {
                                    locais.forEach { local ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    SelectedPlaceSession.place = local
                                                    mostrarPopup = false
                                                    val placeId = (local.nome ?: "local").ifBlank { "local" }
                                                    navController.navigate(Screens.DetalhesLocal.createRoute(placeId))
                                                },
                                            elevation = CardDefaults.cardElevation(2.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(local.nome)
                                                if (!local.endereco.isNullOrBlank()) Text(local.endereco!!, color = Color.Gray, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EvaluationCard(
    evaluation: Evaluation
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Local: ${evaluation.placeId}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row { Text("Estacionamento: "); Spacer(modifier = Modifier.width(4.dp)); Text(ratingLabel(evaluation.parking)) }
            Row { Text("Entrada Principal: "); Spacer(modifier = Modifier.width(4.dp)); Text(ratingLabel(evaluation.mainEntrance)) }
            Row { Text("Circulação Interna: "); Spacer(modifier = Modifier.width(4.dp)); Text(ratingLabel(evaluation.internalCirculation)) }
            Row { Text("Banheiros: "); Spacer(modifier = Modifier.width(4.dp)); Text(ratingLabel(evaluation.bathrooms)) }
        }
    }
}

private fun ratingLabel(value: Int): String = when (value) {
    0 -> "Sim"
    1 -> "Não"
    2 -> "Não se aplica"
    else -> "-"
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