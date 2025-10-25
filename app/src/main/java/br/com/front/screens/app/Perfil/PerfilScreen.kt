package br.com.front.screens.app

import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.front.data.session.UserSession
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.front.ui.viewmodel.ProfileViewModel
import br.com.front.ui.viewmodel.UpdateUserState
import android.util.Patterns
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import br.com.front.data.session.UserDataStore

@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    navController: NavHostController
) {
    val user = UserSession.user.collectAsState().value
    var showEditDialog by remember { mutableStateOf(false) }
    var editedNome by remember { mutableStateOf(user?.nome ?: "") }
    var editedCpf by remember { mutableStateOf("") }
    var editedEmail by remember { mutableStateOf(user?.email ?: "") }
    var editedSenha by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }

    val profileViewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current
    val store = remember { UserDataStore(context) }
    LaunchedEffect(Unit) { profileViewModel.refreshUser(store) }
    val updateState by profileViewModel.updateState.collectAsState()
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
                            text = "Olá, ${user?.nome ?: "Usuário"}",
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
                    titulo = "Dados Pessoais",
                    nome = user?.nome ?: "",
                    "", "", "", ""
                , onEdit = {
                        editedNome = user?.nome ?: ""
                        editedEmail = user?.email ?: ""
                        // cpf/senha não estão no UserResponse, usuário pode preencher
                        showEditDialog = true
                    }
                )
                DadosSecaoSociais("Visual", "Bolsa Família", onEdit = { showEditDialog = true })
                DadosSecaoOcupacao("Estudante", "Até 1 salário mínimo", onEdit = { showEditDialog = true })
                // Endereço removido por não haver dados no UserResponse atual. Renderize quando o backend fornecer estes campos.

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
                onClick = {
                    UserSession.clear()
                    onLogout()
                },
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

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = {
                if (updateState !is UpdateUserState.Loading) {
                    showEditDialog = false
                    profileViewModel.resetState()
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val current = UserSession.user.value
                    if (current != null && updateState !is UpdateUserState.Loading) {
                        // Basic validation matching backend constraints
                        val cpfRegex = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}".toRegex()
                        val senhaHasLen = editedSenha.length >= 8
                        val senhaHasNum = editedSenha.any { it.isDigit() }
                        val senhaHasLetter = editedSenha.any { it.isLetter() }
                        val emailValid = Patterns.EMAIL_ADDRESS.matcher(editedEmail).matches()

                        formError = when {
                            editedNome.isBlank() -> "Nome é obrigatório"
                            !cpfRegex.matches(editedCpf) -> "CPF inválido (formato 000.000.000-00)"
                            editedEmail.isBlank() || !emailValid -> "Email inválido"
                            !(senhaHasLen && senhaHasNum && senhaHasLetter) -> "Senha deve ter pelo menos 8 caracteres, com número e letra"
                            else -> null
                        }

                        if (formError == null) {
                            profileViewModel.updateUser(
                                id = current.id,
                                nome = editedNome,
                                cpf = editedCpf,
                                email = editedEmail,
                                senha = editedSenha,
                                store = store
                            )
                        }
                    }
                }, enabled = updateState !is UpdateUserState.Loading) {
                    Text(if (updateState is UpdateUserState.Loading) "Salvando..." else "Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    if (updateState !is UpdateUserState.Loading) {
                        showEditDialog = false
                        profileViewModel.resetState()
                    }
                }) { Text("Cancelar") }
            },
            title = { Text(text = "Editar dados do usuário") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedNome,
                        onValueChange = { editedNome = it },
                        singleLine = true,
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editedCpf,
                        onValueChange = { editedCpf = it },
                        singleLine = true,
                        label = { Text("CPF (000.000.000-00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editedEmail,
                        onValueChange = { editedEmail = it },
                        singleLine = true,
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editedSenha,
                        onValueChange = { editedSenha = it },
                        singleLine = true,
                        label = { Text("Senha (min 8, com número e letra)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (formError != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(formError ?: "", color = Color.Red)
                    }
                    if (updateState is UpdateUserState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text((updateState as UpdateUserState.Error).message, color = Color.Red)
                    }
                }
            }
        )
    }

    // Observa sucesso do update para atualizar sessão e fechar
    when (val state = updateState) {
        is UpdateUserState.Success -> {
            // Atualiza sessão (nome/email do UserResponse retornado)
            UserSession.setUser(state.user)
            // Persistir também no DataStore
            LaunchedEffect(state.user) { store.save(state.user) }
            showEditDialog = false
            profileViewModel.resetState()
        }
        else -> Unit
    }
}

@Composable
fun DadosSecao(
    titulo: String,
    nome: String,
    cpf: String,
    nascimento: String,
    sexo: String,
    raca: String,
    onEdit: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader(titulo, onEdit)
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
fun DadosSecaoSociais(deficiencia: String, beneficio: String, onEdit: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Dados Sociais", onEdit)
        DadoItem("Deficiência", deficiencia, fullWidth = true)
        DadoItem("Benefício", beneficio, fullWidth = true)
    }
}

@Composable
fun DadosSecaoOcupacao(ocupacao: String, renda: String, onEdit: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Renda e Ocupação", onEdit)
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
    estado: String,
    onEdit: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SecaoHeader("Meu Endereço", onEdit)
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
fun SecaoHeader(titulo: String, onEdit: (() -> Unit)? = null) {
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
            modifier = Modifier
                .padding(vertical = 8.dp)
                .then(if (onEdit != null) Modifier.clickable { onEdit() } else Modifier)
        )
    }
    Divider(color = Color.LightGray, thickness = 1.dp)
}

