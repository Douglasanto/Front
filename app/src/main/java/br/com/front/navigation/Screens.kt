package br.com.front.navigation

import android.util.Log
import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.com.front.data.RegisterData
import br.com.front.data.toJsonSafe
import java.net.URLEncoder

sealed class Screens(val route: String) {
    // Autenticação
    object Login : Screens("login")
    object SignUp : Screens("signup")

    // Cadastro
    object PersonData : Screens("personData?registerData={registerData}") {
        fun createRoute(registerData: RegisterData): String {
            return try {
                val json = registerData.toJsonSafe()
                val encodedJson = URLEncoder.encode(json, "UTF-8")
                Log.d("Navigation", "Navegando para PersonData com dados: $json")
                "personData?registerData=$encodedJson"
            } catch (e: Exception) {
                Log.e("Navigation", "Erro ao criar rota PersonData", e)
                "personData"
            }
        }
        
        val arguments = listOf(
            navArgument("registerData") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    }
    
    object PersonalAddress : Screens("personalAddress?registerData={registerData}") {
        fun createRoute(registerData: RegisterData): String {
            return try {
                val json = registerData.toJsonSafe()
                val encodedJson = URLEncoder.encode(json, "UTF-8")
                Log.d("Navigation", "Navegando para PersonalAddress com dados: $json")
                "personalAddress?registerData=$encodedJson"
            } catch (e: Exception) {
                Log.e("Navigation", "Erro ao criar rota PersonalAddress", e)
                "personalAddress"
            }
        }
        
        val arguments = listOf(
            navArgument("registerData") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    }
    
    object Socioeconomic : Screens("socioeconomic?registerData={registerData}") {
        fun createRoute(registerData: RegisterData): String {
            return try {
                val json = registerData.toJsonSafe()
                val encodedJson = URLEncoder.encode(json, "UTF-8")
                Log.d("Navigation", "Navegando para Socioeconomic com dados: $json")
                "socioeconomic?registerData=$encodedJson"
            } catch (e: Exception) {
                Log.e("Navigation", "Erro ao criar rota Socioeconomic", e)
                "socioeconomic"
            }
        }
        
        val arguments = listOf(
            navArgument("registerData") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    }

    // App Principal
    object Home : Screens("home")
    object Avaliacoes : Screens("avaliacoes")
    object Mensagens : Screens("mensagens")
    object Perfil : Screens("perfil")

    // Detalhes
    object DetalhesLocal : Screens("detalhes/{placeId}") {
        fun createRoute(placeId: String) = "detalhes/$placeId"
    }
    
    companion object {
        const val REGISTER_DATA_KEY = "registerData"
    }
}

// Use RegisterData.toJsonSafe() from br.com.front.data.RegisterData