package br.com.front.screens.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.front.data.RegisterData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    initialData: RegisterData = RegisterData(),
    onNavigateToAddress: (RegisterData) -> Unit,
    onBack: () -> Unit
) {
    // Initialize state with initialData
    var nome by remember { mutableStateOf(initialData.nome) }
    var cpf by remember { mutableStateOf(initialData.cpf) }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var selectedGender by remember { 
        mutableStateOf(
            when(initialData.sexo?.uppercase()) {
                "M" -> "Masculino"
                "F" -> "Feminino"
                else -> ""
            }
        ) 
    }
    // Cor (raça/cor) dropdown state
    var selectedCor by remember { mutableStateOf(initialData.cor ?: "") }
    var corExpanded by remember { mutableStateOf(false) }
    val corOptions = listOf("Preto", "Branco", "Pardo", "Amarelo")
    
    // Definição das opções de deficiência
    val deficienciaOptions = listOf(
        "Auditiva", "Física", "Visual", "Intelectual", "Outra"
    )
    
    // Estado para as deficiências selecionadas
    var selectedDeficiencias by remember { 
        mutableStateOf(initialData.defs.toMutableStateList()) 
    }
    
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Parse initial data
    LaunchedEffect(initialData) {
        initialData.data_nascimento?.split("/")?.takeIf { it.size == 3 }?.let { dateParts ->
            dia = dateParts[0]
            mes = dateParts[1]
            ano = dateParts[2]
        }
    }

    fun validateForm(): Boolean {
        return when {
            cpf.length != 11 -> {
                errorMessage = "CPF deve ter 11 dígitos"
                false
            }
            dia.isBlank() || mes.isBlank() || ano.isBlank() -> {
                errorMessage = "Por favor, preencha a data de nascimento"
                false
            }
            selectedGender.isBlank() -> {
                errorMessage = "Por favor, selecione o gênero"
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Conteúdo rolável
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // Espaço para o botão
        ) {
            // Cabeçalho
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Voltar",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBack() }
                )
                Text(
                    text = "Dados Pessoais",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Conteúdo do formulário
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = cpf,
                    onValueChange = {
                        if (it.length <= 11) {
                            cpf = it.filter { char -> char.isDigit() }
                        }
                    },
                    label = { Text("CPF") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Data de Nascimento",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(0.8f), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = dia,
                        onValueChange = { if (it.length <= 2) dia = it },
                        label = { Text("Dia") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = mes,
                        onValueChange = { if (it.length <= 2) mes = it },
                        label = { Text("Mês") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = ano,
                        onValueChange = { if (it.length <= 4) ano = it },
                        label = { Text("Ano") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Cor (Raça/Cor)
                Text(
                    text = "Cor",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = corExpanded,
                    onExpandedChange = { corExpanded = !corExpanded }
                ) {
                    OutlinedTextField(
                        value = if (selectedCor.isBlank()) "Escolha uma das opções" else selectedCor,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = corExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = corExpanded,
                        onDismissRequest = { corExpanded = false }
                    ) {
                        corOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedCor = option
                                    corExpanded = false
                                }
                            )
                        }
                    }
                }
                Text(
                    text = "Sexo",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selectedGender = "Masculino" }
                            .padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedGender == "Masculino",
                            onClick = null
                        )
                        Text("Masculino")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selectedGender = "Feminino" }
                    ) {
                        RadioButton(
                            selected = selectedGender == "Feminino",
                            onClick = null
                        )
                        Text("Feminino")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Autodeclaração de raça, cor ou etnia",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    val optionsEtnia = listOf("Branco", "Preto", "Pardo", "Amarelo", "Indígena", "Outro")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedOption by remember { mutableStateOf<String?>(null) }

                    OutlinedTextField(
                        value = selectedOption ?: "Escolha uma das opções",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (expanded) "Fechar" else "Abrir",
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        optionsEtnia.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Deficiência",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                FlowRow(
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    deficienciaOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedDeficiencias.contains(option)) {
                                        selectedDeficiencias.remove(option)
                                    } else {
                                        selectedDeficiencias.add(option)
                                    }
                                }
                        ) {
                            Checkbox(
                                checked = selectedDeficiencias.contains(option),
                                onCheckedChange = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = option,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp)) // Espaço antes do botão
            }
        }
        
        // Botão fixo na parte inferior
        Button(
            onClick = {
                if (validateForm()) {
                    try {
                        val dataNascimento = "$dia/$mes/$ano"
                        val sexo = when (selectedGender) {
                            "Masculino" -> "M"
                            "Feminino" -> "F"
                            else -> null
                        }
                        
                        val updatedData = initialData.copy(
                            nome = nome.trim(),
                            cpf = cpf.filter { it.isDigit() },
                            data_nascimento = dataNascimento,
                            sexo = sexo,
                            defs = selectedDeficiencias.toList(),
                            cor = selectedCor.ifBlank { null }
                        )
                        
                        onNavigateToAddress(updatedData)
                    } catch (e: Exception) {
                        errorMessage = "Erro ao processar os dados: ${e.message}"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Próximo")
            }
        }
    }
}
