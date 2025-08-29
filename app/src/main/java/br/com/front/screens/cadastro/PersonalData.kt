package br.com.front.screens.cadastro

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PersonScreen(
    onNavigateToAddress: () -> Unit,
    onBack: () -> Unit
) {
    var nomeSocial by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Masculino") }
    val options = listOf("Branco", "Preto", "Pardo", "Amarelo", "Indígena", "Outro")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val opcoes = listOf("Auditiva", "Física", "Visual", "Intelectual")
    val selecionadas = remember { mutableStateMapOf<String, Boolean>() }

    opcoes.forEach { opcao ->
        if (selecionadas[opcao] == null) {
            selecionadas[opcao] = false
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Voltar",
            modifier = Modifier.clickable {
                onBack()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Dados Pessoais",
                    color = Color.Black,
                    fontSize = 14.sp,
                )
                Divider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nomeSocial,
            onValueChange = { nomeSocial = it },
            label = { Text("Nome Social (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cpf,
            onValueChange = { cpf = it },
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
                options.forEach { option ->
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
            text = "Indique qual tipo de deficiencia você possui",
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 0 until opcoes.size step 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (j in 0..1) {
                        val opcao = opcoes[i + j]

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f).padding(8.dp)
                        ) {
                            Checkbox(
                                checked = selecionadas[opcao] ?: false,
                                onCheckedChange = { selecionadas[opcao] = it }
                            )
                            Text(text = opcao)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onNavigateToAddress()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Avançar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val selected = index == 0
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (selected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (selected) Color.Blue else Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
