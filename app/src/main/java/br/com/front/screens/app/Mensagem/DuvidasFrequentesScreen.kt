package br.com.front.screens.app.Mensagem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuvidasFrequentesScreen(
    navController: NavController
) {
    val perguntasRespostas = listOf(
        "Como posso relatar um problema de acessibilidade?" to "Você pode relatar um problema de acessibilidade através do menu principal, selecionando 'Relatar Problema' e preenchendo o formulário.",
        "Quais são os horários de funcionamento do atendimento?" to "Nosso atendimento está disponível de segunda a sexta, das 8h às 18h.",
        "Como faço para sugerir uma melhoria?" to "Você pode enviar sugestões através do menu 'Mensagens' > 'Enviar Sugestão'.",
        "Onde vejo o status do meu relato?" to "O status dos seus relatos pode ser acompanhado na seção 'Meus Relatos' no menu principal.",
        "Como funciona o sistema de notificações?" to "Você receberá notificações sobre atualizações em seus relatos e mensagens importantes do sistema."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dúvidas Frequentes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(perguntasRespostas) { (pergunta, resposta) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = pergunta,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resposta,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DuvidasFrequentesScreenPreview() {
    DuvidasFrequentesScreen(navController = NavHostController(LocalContext.current))
}
