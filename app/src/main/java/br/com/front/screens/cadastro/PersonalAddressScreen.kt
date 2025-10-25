package br.com.front.screens.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import android.util.Log
import br.com.front.data.RegisterData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalAddressScreen(
    initialData: RegisterData = RegisterData(),
    onNavigateToSocioEconomic: (RegisterData) -> Unit,
    onBack: () -> Unit
) {
    var cep by remember { mutableStateOf(initialData.cep) }
    var cidade by remember { mutableStateOf(initialData.cidade) }
    var estado by remember { mutableStateOf(initialData.estado) }
    var bairro by remember { mutableStateOf(initialData.bairro) }
    var logradouro by remember { mutableStateOf(initialData.logradouro) }
    var numero by remember { mutableStateOf(initialData.numero) }
    var complemento by remember { mutableStateOf(initialData.complemento) }
    var tipoMoradiaExpanded by remember { mutableStateOf(false) }
    var tipoMoradia by remember { mutableStateOf(initialData.tipo_moradia ?: "") }
    var transporteSelecionado by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val tiposDeMoradia = listOf("Casa", "Apartamento", "Outro")
    val transportes = listOf("A Pé", "Aplicativo de Carona", "Transporte Próprio", "Transporte Público")

    LaunchedEffect(initialData) {
        cep = initialData.cep
        cidade = initialData.cidade
        estado = initialData.estado
        bairro = initialData.bairro
        logradouro = initialData.logradouro
        numero = initialData.numero
        complemento = initialData.complemento
        tipoMoradia = initialData.tipo_moradia ?: ""
    }

    fun validateForm(): Boolean {
        return when {
            cep.length < 8 -> {
                errorMessage = "Por favor, insira um CEP válido"
                false
            }
            cidade.isBlank() -> {
                errorMessage = "Por favor, insira uma cidade"
                false
            }
            estado.isBlank() -> {
                errorMessage = "Por favor, insira um estado"
                false
            }
            bairro.isBlank() -> {
                errorMessage = "Por favor, insira um bairro"
                false
            }
            logradouro.isBlank() -> {
                errorMessage = "Por favor, insira um logradouro"
                false
            }
            numero.isBlank() -> {
                errorMessage = "Por favor, insira um número"
                false
            }
            tipoMoradia.isBlank() -> {
                errorMessage = "Por favor, selecione o tipo de moradia"
                false
            }
            transporteSelecionado.isBlank() -> {
                errorMessage = "Por favor, selecione seu meio de transporte"
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "Endereço Pessoal",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = cep,
                onValueChange = {
                    if (it.length <= 8) {
                        cep = it.filter { char -> char.isDigit() }
                    }
                },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    label = { Text("Cidade") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("UF") },
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = logradouro,
                onValueChange = { logradouro = it },
                label = { Text("Logradouro") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("Número") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = complemento,
                    onValueChange = { complemento = it },
                    label = { Text("Complemento") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = tipoMoradiaExpanded,
                onExpandedChange = { tipoMoradiaExpanded = !tipoMoradiaExpanded }
            ) {
                OutlinedTextField(
                    value = tipoMoradia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de moradia") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoMoradiaExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = tipoMoradiaExpanded,
                    onDismissRequest = { tipoMoradiaExpanded = false }
                ) {
                    tiposDeMoradia.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoMoradia = tipo
                                tipoMoradiaExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Qual o seu principal meio de transporte?",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                transportes.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { transporte ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (transporteSelecionado == transporte) 
                                            MaterialTheme.colorScheme.primaryContainer 
                                        else 
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable { transporteSelecionado = transporte }
                                    .padding(8.dp)
                            ) {
                                RadioButton(
                                    selected = transporteSelecionado == transporte,
                                    onClick = { transporteSelecionado = transporte }
                                )
                                Text(
                                    text = transporte,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (validateForm()) {
                    try {
                        Log.d("PersonalAddressScreen", "Formulário validado com sucesso")
                        val updatedData = initialData.copy(
                            cep = cep.filter { it.isDigit() },
                            logradouro = logradouro.trim(),
                            numero = numero.trim(),
                            complemento = complemento.trim(),
                            bairro = bairro.trim(),
                            cidade = cidade.trim(),
                            estado = estado.trim(),
                            tipo_moradia = tipoMoradia.ifEmpty { null },
                            tipoTransporte = transporteSelecionado.ifEmpty { null }
                        )
                        
                        Log.d("PersonalAddressScreen", "Dados atualizados: $updatedData")
                        onNavigateToSocioEconomic(updatedData)
                        Log.d("PersonalAddressScreen", "Navegação para dados socioeconômicos iniciada")
                    } catch (e: Exception) {
                        Log.e("PersonalAddressScreen", "Erro ao processar endereço", e)
                        errorMessage = "Erro ao processar o endereço: ${e.message}"
                    }
                } else {
                    Log.e("PersonalAddressScreen", "Falha na validação: $errorMessage")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Próximo")
            }
        }
    }
}
