package br.com.front.screens.app.Mensagem

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MensagensScreen(
    navController: NavHostController,
) {

    val context = LocalContext.current

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
                    .height(120.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF2D6CDF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mensagens",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MensagemCaixa(
                    titulo = "Olá, SAULO",
                    conteudo = "Por aqui você pode retirar dúvidas e enviar feedbacks ou sugestões para contribuir para uma cidade mais acessível e inclusiva.",
                    data = "16/11/2023 - 8:48"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "O que você deseja fazer?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OpcaoBotao(
                    texto = "Dúvidas Frequentes",
                    onClick = {
                        navController.navigate("duvidas_frequentes")
                    }
                )

                OpcaoBotao(
                    texto = "Conversar com Atendente",
                    onClick = {
                        navController.navigate("chat_atendente")
                    }
                )

                OpcaoBotao(
                    texto = "Enviar Feedback",
                    onClick = {
                        navController.navigate("formulario_mensagem/feedback")
                    }
                )

                OpcaoBotao(
                    texto = "Enviar Sugestão",
                    onClick = {
                        navController.navigate("formulario_mensagem/sugestao")
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Data final
                Text(
                    text = "16/11/2023 - 8:48",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun MensagemCaixa(titulo: String, conteudo: String, data: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = data,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = conteudo,
            fontSize = 14.sp
        )
    }
}

@Composable
fun OpcaoBotao(texto: String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF2D6CDF)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(
            text = texto,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun MensagensNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "mensagens_principal"
    ) {
        composable("mensagens_principal") {
            MensagensScreen(navController = navController)
        }
        composable("duvidas_frequentes") {
            DuvidasFrequentesScreen(navController = navController)
        }
        composable("chat_atendente") {
            ChatAtendenteScreen(navController = navController)
        }
        composable(
            "formulario_mensagem/{tipo}",
            arguments = listOf(navArgument("tipo") { type = NavType.StringType })
        ) { backStackEntry ->
            val tipo = backStackEntry.arguments?.getString("tipo") ?: ""
            FormularioMensagemScreen(
                navController = navController,
                tipoMensagem = tipo
            )
        }
    }
}

@Preview()
@Composable
fun MensagensScreenPreview() {
    val navController = rememberNavController()
    MensagensNavGraph(navController = navController)
}
