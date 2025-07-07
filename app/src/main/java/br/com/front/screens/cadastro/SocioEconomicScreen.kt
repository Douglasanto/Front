package br.com.front.screens.cadastro

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import br.com.front.ui.components.DropdownField

@Composable
fun SocioeconomicScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit = {}
) {
    var escolaridade by remember { mutableStateOf("") }
    var ocupacao by remember { mutableStateOf("") }
    var renda by remember { mutableStateOf("") }
    var participaPrograma by remember { mutableStateOf("Sim") }
    var programa by remember { mutableStateOf("") }
    var nis by remember { mutableStateOf("") }

    val escolaridadeOptions = listOf("Ensino Fundamental", "Ensino Médio", "Superior", "Pós-graduação")
    val ocupacaoOptions = listOf("Desempregado", "Estudante", "CLT", "Autônomo", "Servidor Público")
    val rendaOptions = listOf("Até 1 salário", "1 a 2 salários", "2 a 5 salários", "Acima de 5 salários")
    val programasSociais = listOf("Bolsa Família", "Auxílio Brasil", "BPC", "Outro")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Voltar",
            modifier = Modifier.clickable { onBack() }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Perfil Socioeconomico",
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

        DropdownField(label = "Escolaridade", options = escolaridadeOptions, selectedOption = escolaridade) {
            escolaridade = it
        }

        DropdownField(label = "Ocupação", options = ocupacaoOptions, selectedOption = ocupacao) {
            ocupacao = it
        }

        DropdownField(label = "Renda Salarial", options = rendaOptions, selectedOption = renda) {
            renda = it
        }

        Text(
            text = "Você participa de algum Programa do Governo Federal, Estadual, Municipal ou recebe algum benefício social?",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            RadioButton(
                selected = participaPrograma == "Não",
                onClick = { participaPrograma = "Não" }
            )
            Text(text = "Não", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = participaPrograma == "Sim",
                onClick = { participaPrograma = "Sim" }
            )
            Text(text = "Sim")
        }

        if (participaPrograma == "Sim") {
            DropdownField(label = "Indique o programa", options = programasSociais, selectedOption = programa) {
                programa = it
            }

            OutlinedTextField(
                value = nis,
                onValueChange = { nis = it },
                label = { Text("Número do NIS") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* ação ao avançar */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Avançar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val isSelected = index == 2
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 6.dp)
                        .background(
                            color = if (isSelected) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}