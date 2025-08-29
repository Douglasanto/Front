package br.com.front.screens.app

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    navController: NavHostController
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF2D6CDF)),
                contentAlignment = Alignment.Center
            )  {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Olá, Saulo",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configurações",
                        tint = Color.White
                    )
                }
            }




            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                DadosSecao(
                    "Dados Pessoais", "Saulo Matos Pereira Gomes",
                    "000.000.000-00", "16/12/1997", "Masculino", "Pardo"
                )
                DadosSecaoSociais("Visual", "Bolsa Família")
                DadosSecaoOcupacao("Estudante", "Até 1 salário mínimo")
                DadosSecaoEndereco(
                    logradouro = "Caminho Padre Vieira",
                    numero = "75",
                    complemento = "A",
                    bairro = "Dois de Julho",
                    cep = "42809-216",
                    cidade = "Camaçari",
                    estado = "BA"
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Mantenha os seus dados atualizados",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp)
                )
            }

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                )
            ) {
                Text("Sair da Conta")
            }
        }
    }
}

@Composable
fun DadosSecao(titulo: String, nome: String, cpf: String, nascimento: String, sexo: String, raca: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader(titulo)
        DadoItem("Nome", nome, fullWidth = true)

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                DadoItem("CPF", cpf)
            }

            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Data de Nascimento", nascimento)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Sexo", sexo)
            }

            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Raça", raca)
            }
        }
    }
}

@Composable
fun DadosSecaoSociais(deficiencia: String, beneficio: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Dados Sociais")
        DadoItem("Deficiência", deficiencia, fullWidth = true)
        DadoItem("Benefício", beneficio, fullWidth = true)
    }
}

@Composable
fun DadosSecaoOcupacao(ocupacao: String, renda: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Renda e Ocupação")
        DadoItem("Ocupação", ocupacao, fullWidth = true)
        DadoItem("Renda", renda, fullWidth = true)
    }
}

@Composable
fun DadosSecaoEndereco(
    logradouro: String,
    numero: String,
    complemento: String,
    bairro: String,
    cep: String,
    cidade: String,
    estado: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Meu Endereço")
        DadoItem("Logradouro", logradouro, fullWidth = true)

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Número", numero)
            }

            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Complemento", complemento)
            }
        }

        DadoItem("Bairro", bairro, fullWidth = true)

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Cidade", cidade)
            }
            Box(modifier = Modifier.weight(1f)) {
                DadoItem("Estado", estado)
            }
        }

        DadoItem("CEP", cep, fullWidth = true)
    }
}

@Composable
fun DadoItem(rotulo: String, valor: String, fullWidth: Boolean = false) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
    ) {
        Text(
            text = rotulo,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = valor,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 2.dp)
        )

    }
}

@Composable
fun SecaoHeader(titulo: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "editar",
            color = Color(0xFF2D6CDF),
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
    Divider(color = Color.LightGray, thickness = 1.dp)
}

