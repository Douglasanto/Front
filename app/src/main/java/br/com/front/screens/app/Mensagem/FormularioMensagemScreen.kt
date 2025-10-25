package br.com.front.screens.app.Mensagem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMensagemScreen(
    navController: NavController,
    tipoMensagem: String
) {
    var titulo by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    var isEnviando by remember { mutableStateOf(false) }
    
    val tituloTela = when (tipoMensagem.lowercase()) {
        "feedback" -> "Enviar Feedback"
        "sugestao" -> "Enviar Sugestão"
        else -> "Enviar Mensagem"
    }
    
    val placeholderMensagem = when (tipoMensagem.lowercase()) {
        "feedback" -> "Descreva seu feedback detalhadamente..."
        "sugestao" -> "Descreva sua sugestão detalhadamente..."
        else -> "Digite sua mensagem..."
    }
    
    fun enviarMensagem() {
        if (titulo.isNotBlank() && mensagem.isNotBlank()) {
            isEnviando = true
            // Aqui você implementaria a lógica para enviar a mensagem para o servidor
            // Por exemplo, usando uma ViewModel ou um serviço
            
            // Simula o envio (remova isso na implementação real)
            /*
            viewModel.enviarMensagem(
                tipo = tipoMensagem,
                titulo = titulo,
                mensagem = mensagem,
                onSuccess = {
                    isEnviando = false
                    navController.popBackStack()
                    // Mostrar mensagem de sucesso
                },
                onError = {
                    isEnviando = false
                    // Mostrar mensagem de erro
                }
            )
            */
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloTela) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { enviarMensagem() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = titulo.isNotBlank() && mensagem.isNotBlank() && !isEnviando
            ) {
                if (isEnviando) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Text("Enviando...")
                } else {
                    Text("Enviar $tipoMensagem")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Preencha o formulário abaixo para enviar seu ${tipoMensagem.lowercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                isError = titulo.isBlank()
            )
            
            OutlinedTextField(
                value = mensagem,
                onValueChange = { mensagem = it },
                label = { Text("Mensagem") },
                placeholder = { Text(placeholderMensagem) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .heightIn(min = 200.dp),
                isError = mensagem.isBlank(),
                maxLines = 10
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Agradecemos por contribuir para melhorar nossos serviços!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun FormularioMensagemScreenPreview() {
    FormularioMensagemScreen(navController = rememberNavController(), tipoMensagem = "Mensagem")
}

