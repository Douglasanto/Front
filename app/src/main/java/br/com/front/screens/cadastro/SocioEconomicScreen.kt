package br.com.front.screens.cadastro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.com.front.data.RegisterData
import br.com.front.data.api.RetrofitInstance.apiService
import br.com.front.data.model.request.UsuarioFullRequest
import br.com.front.data.model.request.EnderecoRequest
import br.com.front.data.model.request.BeneficioRequest
import br.com.front.data.model.request.ProfissaoRequest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import android.util.Log
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import br.com.front.util.LocationHelper

data class SocioEconomicData(
    val escolaridade: String = "",
    val ocupacao: String = "",
    val renda: String = "",
    val participaPrograma: String = "Sim",
    val programa: String = "",
    val nis: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocioeconomicScreen(
    initialData: RegisterData,
    onRegisterComplete: (String?) -> Unit,
    onBack: () -> Unit
) {
    var escolaridade by remember { mutableStateOf(initialData.escolaridade ?: "") }
    // Renda (faixa) via dropdown
    var rendaFaixa by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf(initialData.estado_civil ?: "") }
    var tipoMoradia by remember { mutableStateOf(initialData.tipo_moradia ?: "") }
    var ocupacao by remember { mutableStateOf(initialData.ocupacao ?: "") } // Profissão (texto livre)
    var beneficioDesc by remember { mutableStateOf("") } // Benefício (opcional)
    var participaPrograma by remember { mutableStateOf(initialData.participaPrograma ?: "Sim") }
    var programa by remember { mutableStateOf(initialData.programa ?: "") }
    var nis by remember { mutableStateOf(initialData.nis ?: "") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Opções para os dropdowns
    val escolaridadeOptions = listOf(
        "Ensino Fundamental Incompleto",
        "Ensino Fundamental Completo",
        "Ensino Médio Incompleto",
        "Ensino Médio Completo",
        "Ensino Superior Incompleto",
        "Ensino Superior Completo",
        "Pós-graduação",
        "Mestrado/Doutorado"
    )
    
    val estadoCivilOptions = listOf(
        "Solteiro(a)",
        "Casado(a)",
        "Divorciado(a)",
        "Viúvo(a)",
        "Separado(a)",
        "União Estável"
    )
    
    val tipoMoradiaOptions = listOf(
        "Própria quitada",
        "Própria financiada",
        "Alugada",
        "Cedida",
        "Com parentes",
        "Outro"
    )
    
    val ocupacaoOptions = listOf(
        "Desempregado",
        "Estudante",
        "CLT",
        "Autônomo",
        "Servidor Público"
    )
    
    val programasSociais = listOf(
        "Bolsa Família",
        "Auxílio Brasil",
        "BPC",
        "Outro"
    )

    val scrollState = rememberScrollState()
    // Dropdown states
    var rendaExpanded by remember { mutableStateOf(false) }

    // Localização: permissões e helper
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { /* no-op */ }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    // Expanded states for dropdowns
    var escolaridadeExpanded by remember { mutableStateOf(false) }
    var estadoCivilExpanded by remember { mutableStateOf(false) }
    var tipoMoradiaExpanded by remember { mutableStateOf(false) }
    var ocupacaoExpanded by remember { mutableStateOf(false) }
    var programaExpanded by remember { mutableStateOf(false) }
    
    fun validateForm(): Boolean {
        return when {
            escolaridade.isBlank() -> {
                errorMessage = "Por favor, selecione a escolaridade"
                false
            }
            rendaFaixa.isBlank() -> {
                errorMessage = "Por favor, informe a renda mensal"
                false
            }
            estadoCivil.isBlank() -> {
                errorMessage = "Por favor, selecione o estado civil"
                false
            }
            tipoMoradia.isBlank() -> {
                errorMessage = "Por favor, selecione o tipo de moradia"
                false
            }
            ocupacao.isBlank() -> {
                errorMessage = "Por favor, selecione a ocupação"
                false
            }
            participaPrograma == "Sim" && programa.isBlank() -> {
                errorMessage = "Por favor, selecione o programa social"
                false
            }
            participaPrograma == "Sim" && nis.isBlank() -> {
                errorMessage = "Por favor, informe o NIS"
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
    ) {
        // Coluna principal com rolagem
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp) // Adiciona espaço para o botão no final
        ) {
            // Cabeçalho
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
                Text(
                    text = "Dados Socioeconômicos",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conteúdo do formulário
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Escolaridade
                ExposedDropdownMenuBox(
                    expanded = escolaridadeExpanded,
                    onExpandedChange = { escolaridadeExpanded = !escolaridadeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = escolaridade,
                        onValueChange = {},
                        label = { Text("Escolaridade") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = escolaridadeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = escolaridadeExpanded,
                        onDismissRequest = { escolaridadeExpanded = false }
                    ) {
                        escolaridadeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    escolaridade = option
                                    escolaridadeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profissão (texto livre)
                OutlinedTextField(
                    value = ocupacao,
                    onValueChange = { ocupacao = it },
                    label = { Text("Profissão") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Benefício (opcional) - texto livre
                OutlinedTextField(
                    value = beneficioDesc,
                    onValueChange = { beneficioDesc = it },
                    label = { Text("Benefício (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Renda Mensal (faixa)
                val rendaOptions = listOf(
                    "Até 1 salário mínimo",
                    "Entre 1 e 5 salários mínimos",
                    "Entre 5 e 10 salários mínimos",
                    "Acima de 10 salários mínimos"
                )
                ExposedDropdownMenuBox(
                    expanded = rendaExpanded,
                    onExpandedChange = { rendaExpanded = !rendaExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = if (rendaFaixa.isBlank()) "Selecione a renda mensal" else rendaFaixa,
                        onValueChange = {},
                        label = { Text("Renda Mensal") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rendaExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = rendaExpanded,
                        onDismissRequest = { rendaExpanded = false }
                    ) {
                        rendaOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    rendaFaixa = option
                                    rendaExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Estado Civil
                ExposedDropdownMenuBox(
                    expanded = estadoCivilExpanded,
                    onExpandedChange = { estadoCivilExpanded = !estadoCivilExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = estadoCivil,
                        onValueChange = {},
                        label = { Text("Estado Civil") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoCivilExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = estadoCivilExpanded,
                        onDismissRequest = { estadoCivilExpanded = false }
                    ) {
                        estadoCivilOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    estadoCivil = option
                                    estadoCivilExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tipo de Moradia
                ExposedDropdownMenuBox(
                    expanded = tipoMoradiaExpanded,
                    onExpandedChange = { tipoMoradiaExpanded = !tipoMoradiaExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = tipoMoradia,
                        onValueChange = {},
                        label = { Text("Tipo de Moradia") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoMoradiaExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = tipoMoradiaExpanded,
                        onDismissRequest = { tipoMoradiaExpanded = false }
                    ) {
                        tipoMoradiaOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    tipoMoradia = option
                                    tipoMoradiaExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ocupação
                ExposedDropdownMenuBox(
                    expanded = ocupacaoExpanded,
                    onExpandedChange = { ocupacaoExpanded = !ocupacaoExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = ocupacao,
                        onValueChange = {},
                        label = { Text("Ocupação") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ocupacaoExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = ocupacaoExpanded,
                        onDismissRequest = { ocupacaoExpanded = false }
                    ) {
                        ocupacaoOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    ocupacao = option
                                    ocupacaoExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Participa de Programa Social
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
                    Spacer(modifier = Modifier.height(16.dp))

                    // Programa Social
                    ExposedDropdownMenuBox(
                        expanded = programaExpanded,
                        onExpandedChange = { programaExpanded = !programaExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = programa,
                            onValueChange = {},
                            label = { Text("Programa Social") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = programaExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = programaExpanded,
                            onDismissRequest = { programaExpanded = false }
                        ) {
                            programasSociais.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        programa = option
                                        programaExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // NIS
                    OutlinedTextField(
                        value = nis,
                        onValueChange = { nis = it },
                        label = { Text("NIS") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Mensagem de erro
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
                    scope.launch {
                        try {
                            isLoading = true
                            Log.d("SocioeconomicScreen", "Iniciando cadastro do usuário...")
                            
                            val normalizedEmail = (initialData.email ?: "").trim().lowercase()
                            val rawPhone = (initialData.telefone ?: "").filter { it.isDigit() }
                            val phoneLong = rawPhone.toLongOrNull() ?: 0L
                            val cpfDigits = (initialData.cpf ?: "").filter { it.isDigit() }
                            val formattedCpf = if (cpfDigits.length == 11) {
                                "${cpfDigits.substring(0,3)}.${cpfDigits.substring(3,6)}.${cpfDigits.substring(6,9)}-${cpfDigits.substring(9,11)}"
                            } else cpfDigits

                            // Map UI to backend enums
                            fun mapSexo(ui: String?): String? = when (ui) {
                                "Masculino" -> "homem"
                                "Feminino" -> "mulher"
                                else -> null
                            }

                            fun mapEstadoCivil(ui: String?): String? = when (ui) {
                                "Solteiro(a)" -> "solteiro"
                                "Casado(a)" -> "casado"
                                "Divorciado(a)" -> "divorciado"
                                "Viúvo(a)" -> "viuvo"
                                "Separado(a)" -> "separado"
                                "União Estável" -> null // não há correspondente direto
                                else -> null
                            }

                            fun mapCor(ui: String?): String? = when (ui) {
                                "Branco" -> "branco"
                                "Preto" -> "negro"
                                "Pardo" -> "pardo"
                                "Amarelo" -> "amarelo"
                                else -> null
                            }

                            fun mapEscolaridade(ui: String?): String? = when (ui) {
                                "Ensino Médio Completo" -> "ensino_Medio_Completo"
                                "Ensino Médio Incompleto" -> "ensino_Medio_Imcompleto"
                                "Ensino Superior Completo" -> "ensino_Superior_Completo"
                                "Ensino Superior Incompleto" -> "ensino_Superior_Imcompleto"
                                else -> null
                            }

                            fun mapTipoMoradia(ui: String?): String? = when (ui) {
                                "Própria quitada", "Própria financiada" -> "propria"
                                "Alugada" -> "alugada"
                                "Cedida", "Com parentes" -> "favor_familia"
                                else -> null
                            }

                            fun mapRendaMensal(ui: String?): String? = when (ui) {
                                "Até 1 salário mínimo" -> "ate_1_salário_mínimo"
                                "Entre 1 e 5 salários mínimos" -> "entre_1_e_5_salários_mínimos"
                                "Entre 5 e 10 salários mínimos" -> "entre_5_e_10_salários_mínimos"
                                "Acima de 10 salários mínimos" -> "acima_de_10_salários_mínimos"
                                else -> null
                            }

                            val endereco = EnderecoRequest(
                                tipo_endereco = "Residencial",
                                logradouro = (initialData.logradouro).trim(),
                                bairro = (initialData.bairro).trim(),
                                cidade = (initialData.cidade).trim(),
                                estado = (initialData.estado).trim(),
                                cep = (initialData.cep).trim()
                            )

                            val selectedGender = null
                            val coords = locationHelper.getLastLocation()
                            val latitude = coords?.first
                            val longitude = coords?.second
                            val dto = UsuarioFullRequest(
                                nome = (initialData.nome ?: "").trim(),
                                nome_social = initialData.nome_social,
                                cpf = formattedCpf,
                                data_nascimento = initialData.data_nascimento, // já no formato dd/MM/yyyy
                                email = normalizedEmail,
                                senha = initialData.senha,
                                telefone = phoneLong,
                                telefone_contato = null,
                                latitude = latitude,
                                longitude = longitude,
                                plusCode = initialData.plusCode,
                                cor = mapCor(initialData.cor),
                                escolaridade = mapEscolaridade(escolaridade),
                                renda_mensal = mapRendaMensal(rendaFaixa),
                                sexo = mapSexo(selectedGender),
                                tipo_moradia = mapTipoMoradia(tipoMoradia),
                                estado_civil = mapEstadoCivil(estadoCivil),
                                deficienciaIds = null,
                                bens = if (beneficioDesc.isNotBlank()) listOf(BeneficioRequest(desc_beneficio = beneficioDesc.trim())) else null,
                                profs = if (ocupacao.isNotBlank()) listOf(ProfissaoRequest(desc_profissao = ocupacao.trim())) else null,
                                enderecos = listOf(endereco)
                            )

                            Log.d("SocioeconomicScreen", "Dados para cadastro (DTO): $dto")

                            val response = apiService.registerUserFull(dto)
                            Log.d("SocioeconomicScreen", "Resposta da API: ${response.code()}")
                            
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody?.success == true) {
                                    Log.d("SocioeconomicScreen", "Cadastro realizado com sucesso!")
                                    val userName = responseBody.user?.nome
                                    onRegisterComplete(userName)
                                } else {
                                    val errorMsg = responseBody?.message ?: "Resposta inválida do servidor"
                                    Log.e("SocioeconomicScreen", "Erro no cadastro: $errorMsg")
                                    errorMessage = errorMsg
                                }
                            } else {
                                val rawError = try { response.errorBody()?.string() } catch (e: Exception) { null }
                                val parsedMsg = try {
                                    rawError?.let {
                                        val json = JsonParser.parseString(it).asJsonObject
                                        when {
                                            json.has("error") && !json.get("error").isJsonNull -> json.get("error").asString
                                            json.has("message") && !json.get("message").isJsonNull -> json.get("message").asString
                                            else -> it
                                        }
                                    }
                                } catch (e: Exception) { null }
                                val errorMsg = parsedMsg?.takeIf { it.isNotBlank() } ?: response.message()
                                Log.e("SocioeconomicScreen", "Erro na requisição (${response.code()}): $errorMsg")
                                errorMessage = "Erro ${response.code()}: $errorMsg"
                            }
                        } catch (e: Exception) {
                            Log.e("SocioeconomicScreen", "Erro ao processar cadastro", e)
                            errorMessage = "Erro inesperado: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    Log.e("SocioeconomicScreen", "Falha na validação: $errorMessage")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Finalizar Cadastro")
            }
        }
    }
}

// Helper para formatação de moeda
class CurrencyTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val formatted = if (digits.isNotEmpty()) {
            val value = (digits.toDoubleOrNull() ?: 0.0) / 100
            NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
        } else ""
        
        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = formatted.length
                override fun transformedToOriginal(offset: Int): Int = offset
            }
        )
    }
}