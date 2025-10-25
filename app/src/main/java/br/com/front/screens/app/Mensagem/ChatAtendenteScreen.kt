package br.com.front.screens.app.Mensagem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.front.screens.app.avaliacoes.AvaliacaoScreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAtendenteScreen(
    navController: NavController
) {
    var mensagem by remember { mutableStateOf("") }
    val mensagens = remember { mutableStateListOf<Mensagem>() }

    LaunchedEffect(Unit) {
        if (mensagens.isEmpty()) {
            mensagens.add(
                Mensagem(
                    texto = "Olá! Sou o atendente virtual. Como posso te ajudar hoje?",
                    ehRemetente = false,
                    horario = SimpleDateFormat("HH:mm").format(Date())
                )
            )
        }
    }

    fun enviarMensagem() {
        if (mensagem.isNotBlank()) {
            mensagens.add(
                Mensagem(
                    texto = mensagem,
                    ehRemetente = true,
                    horario = SimpleDateFormat("HH:mm").format(Date())
                )
            )
            mensagem = ""
            
            // Simula resposta automática após 1 segundo
            // Em uma implementação real, você chamaria sua API de chat aqui
            /*
            LaunchedEffect(Unit) {
                delay(1000)
                mensagens.add(
                    Mensagem(
                        texto = "Obrigado pela sua mensagem. Em breve um de nossos atendentes entrará em contato.",
                        ehRemetente = false,
                        horario = SimpleDateFormat("HH:mm").format(Date())
                    )
                )
            }
            */
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atendente Virtual") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista de mensagens
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                items(mensagens.reversed()) { mensagem ->
                    MensagemItem(mensagem = mensagem)
                }
            }

            // Campo de entrada de mensagem
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = mensagem,
                    onValueChange = { mensagem = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Digite sua mensagem...") },
                    shape = RoundedCornerShape(24.dp)
                )
                
                IconButton(
                    onClick = { enviarMensagem() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MensagemItem(mensagem: Mensagem) {
    val backgroundColor = if (mensagem.ehRemetente) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val horizontalAlignment = if (mensagem.ehRemetente) {
        Alignment.End
    } else {
        Alignment.Start
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (mensagem.ehRemetente) 16.dp else 4.dp,
                bottomEnd = if (mensagem.ehRemetente) 4.dp else 16.dp
            ),
            color = backgroundColor,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = mensagem.texto,
                    color = if (mensagem.ehRemetente) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = mensagem.horario,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (mensagem.ehRemetente) Color.White.copy(alpha = 0.8f) 
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

data class Mensagem(
    val texto: String,
    val ehRemetente: Boolean,
    val horario: String
)

@Preview
@Composable
fun ChatAtendenteScreenPreview() {
    ChatAtendenteScreen(navController = NavHostController(LocalContext.current))
}