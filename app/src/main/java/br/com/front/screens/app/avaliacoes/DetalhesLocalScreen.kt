package br.com.front.screens.app.avaliacoes

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.front.data.session.EvaluationSession
import br.com.front.data.session.UserSession
import br.com.front.screens.app.avaliacoes.model.Evaluation
import br.com.front.data.session.SelectedPlaceSession
import br.com.front.navigation.Screens


// Modelo de dados para Place (deve estar em um arquivo separado normalmente)
data class Place(
    val id: String,
    val name: String,
    val address: String,
    val openingHours: String,
    val description: String
)

data class AccessibilityRatings(
    val parking: Int = -1,          // Estacionamento
    val mainEntrance: Int = -1,     // Entrada Principal
    val internalCirculation: Int = -1, // Circulação Interna
    val bathrooms: Int = -1,        // Banheiros
    val service: Int = -1           // Atendimento
)

// Lista mockada de lugares (em produção, isso viraria uma fonte de dados real)
val samplePlaces = listOf(
    Place(
        id = "1",
        name = "Horto Florestal",
        address = "Av. Comogeri – Centro",
        openingHours = "Todos os dias de 09h às 17h",
        description = "Parque ecológico com diversas espécies nativas"
    ),
    Place(
        id = "2",
        name = "Teatro Cidade",
        address = "Rua das Artes, 123",
        openingHours = "Terça a Domingo, 14h às 20h",
        description = "Teatro municipal com diversas apresentações culturais"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesLocalScreen(
    placeId: String?,
    navController: NavHostController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    // Primeiro tenta usar o local selecionado via sessão (resultado da API Nearby)
    val selected = remember { SelectedPlaceSession.place }
    // Fallback para mocks existentes se nada foi selecionado
    val place = remember(placeId, selected) {
        if (selected != null) {
            Place(
                id = placeId ?: (selected.nome ?: "local"),
                name = selected.nome,
                address = selected.endereco ?: "",
                openingHours = "",
                description = ""
            )
        } else {
            samplePlaces.find { it.id == placeId }
        }
    }

    // Se não encontrar o local, volta para a tela anterior
    if (place == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

// Estado para as avaliações de acessibilidade
    var accessibilityRatings by remember { mutableStateOf(AccessibilityRatings()) }

    // Função para atualizar uma avaliação específica
    fun updateRating(category: String, rating: Int) {
        accessibilityRatings = when (category) {
            "Estacionamento" -> accessibilityRatings.copy(parking = rating)
            "Entrada Principal" -> accessibilityRatings.copy(mainEntrance = rating)
            "Circulação Interna" -> accessibilityRatings.copy(internalCirculation = rating)
            "Banheiros" -> accessibilityRatings.copy(bathrooms = rating)
            "Atendimento" -> accessibilityRatings.copy(service = rating)
            else -> accessibilityRatings
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Local") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Imagem do local
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            )

            // Informações do local
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = place.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = place.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = place.openingHours,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção de Descrição
                Text(
                    text = "Descrição",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = place.description,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Seção de Avaliações
                Text(
                    text = "Avaliações",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                AvaliacaoAcessibilidadeSection(
                    ratings = accessibilityRatings,
                    onRatingChanged = { category, rating ->
                        updateRating(category, rating)
                    }
                )

                // Botão para enviar avaliações
                Button(
                    onClick = {
                        val currentUser = UserSession.user.value
                        if (currentUser != null) {
                            val eval = Evaluation(
                                userId = currentUser.id,
                                placeId = place.id,
                                parking = accessibilityRatings.parking,
                                mainEntrance = accessibilityRatings.mainEntrance,
                                internalCirculation = accessibilityRatings.internalCirculation,
                                bathrooms = accessibilityRatings.bathrooms,
                                service = accessibilityRatings.service,
                                timestamp = System.currentTimeMillis()
                            )
                            EvaluationSession.addEvaluation(eval)
                        }
                        // Limpa seleção e retorna para lista de avaliações
                        SelectedPlaceSession.clear()
                        navController.popBackStack(Screens.Avaliacoes.route, false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Enviar Avaliação")
                }
            }
        }
    }
}

@Composable
private fun AvaliacaoAcessibilidadeSection(
    ratings: AccessibilityRatings,
    onRatingChanged: (String, Int) -> Unit
) {
    Column {
        Text(
            text = "Avaliação de Acessibilidade",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        listOf(
            "Estacionamento" to "Este local possui vagas acessíveis e próximas à entrada principal?",
            "Entrada Principal" to "A entrada principal é acessível para PcD?",
            "Circulação Interna" to "O ambiente interno permite ao usuário autonomia para circular?",
            "Banheiros" to "Possui banheiros adaptados e acessíveis para pessoas com deficiência?",
            "Atendimento" to "O local oferece atendimento e comunicação acessíveis para PcD?"
        ).forEach { (category, question) ->
            val selectedOption = when (category) {
                "Estacionamento" -> ratings.parking
                "Entrada Principal" -> ratings.mainEntrance
                "Circulação Interna" -> ratings.internalCirculation
                "Banheiros" -> ratings.bathrooms
                "Atendimento" -> ratings.service
                else -> -1
            }

            AvaliacaoItem(
                title = category,
                question = question,
                selectedOption = selectedOption,
                onOptionSelected = { rating ->
                    onRatingChanged(category, rating)
                }
            )
        }
    }
}
@Composable
private fun AvaliacaoItem(
    title: String,
    question: String,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Text(
            text = question,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                "Sim" to 0,
                "Não" to 1,
                "Não se aplica" to 2
            ).forEach { (text, value) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == value),
                        onClick = { onOptionSelected(value) }
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}