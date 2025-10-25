import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.front.data.RegisterData
import br.com.front.navigation.Screens
import br.com.front.screens.app.avaliacoes.AvaliacaoScreen
import br.com.front.screens.app.avaliacoes.DetalhesLocalScreen
import br.com.front.screens.app.Home.HomeScreen
import br.com.front.screens.app.Mensagem.MensagensScreen
import br.com.front.screens.app.PerfilScreen
import br.com.front.screens.cadastro.PersonScreen
import br.com.front.screens.cadastro.PersonalAddressScreen
import br.com.front.screens.cadastro.SignUpScreen
import br.com.front.screens.cadastro.SocioeconomicScreen
import br.com.front.screens.login.LoginScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.net.URLDecoder

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screens.SignUp.route)
                },
                onLoginSuccess = { _ ->
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.SignUp.route) {
            SignUpScreen(
                onNavigateToPersonalData = { registerData ->
                    val route = Screens.PersonData.createRoute(registerData)
                    navController.navigate(route)
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // Person Data Screen
        composable(
            route = Screens.PersonData.route,
            arguments = Screens.PersonData.arguments
        ) { backStackEntry ->
            val registerDataJson = backStackEntry.arguments?.getString("registerData")
            val registerData = try {
                if (!registerDataJson.isNullOrEmpty()) {
                    val decoded = URLDecoder.decode(registerDataJson, StandardCharsets.UTF_8.name())
                    Gson().fromJson(decoded, RegisterData::class.java) ?: RegisterData()
                } else {
                    RegisterData()
                }
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error parsing registerData", e)
                RegisterData()
            }
            
            PersonScreen(
                initialData = registerData,
                onNavigateToAddress = { updatedData ->
                    val route = Screens.PersonalAddress.createRoute(updatedData)
                    navController.navigate(route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Personal Address Screen
        composable(
            route = Screens.PersonalAddress.route,
            arguments = Screens.PersonalAddress.arguments
        ) { backStackEntry ->
            val registerDataJson = backStackEntry.arguments?.getString("registerData")
            val registerData = try {
                if (!registerDataJson.isNullOrEmpty()) {
                    val decoded = URLDecoder.decode(registerDataJson, StandardCharsets.UTF_8.name())
                    Gson().fromJson(decoded, RegisterData::class.java) ?: RegisterData()
                } else {
                    RegisterData()
                }
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error parsing registerData", e)
                RegisterData()
            }
            
            PersonalAddressScreen(
                initialData = registerData,
                onNavigateToSocioEconomic = { updatedData ->
                    val route = Screens.Socioeconomic.createRoute(updatedData)
                    navController.navigate(route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Socioeconomic Screen
        composable(
            route = Screens.Socioeconomic.route,
            arguments = Screens.Socioeconomic.arguments
        ) { backStackEntry ->
            val registerDataJson = backStackEntry.arguments?.getString("registerData")
            val registerData = try {
                if (!registerDataJson.isNullOrEmpty()) {
                    val decoded = URLDecoder.decode(registerDataJson, StandardCharsets.UTF_8.name())
                    Gson().fromJson(decoded, RegisterData::class.java) ?: RegisterData()
                } else {
                    RegisterData()
                }
            } catch (e: Exception) {
                Log.e("AppNavigation", "Error parsing registerData", e)
                RegisterData()
            }
            
            SocioeconomicScreen(
                initialData = registerData,
                onRegisterComplete = { _ ->
                    // Após cadastro completo, navega para Home; nome virá do UserSession
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Fluxo Principal
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screens.Avaliacoes.route) {
            AvaliacaoScreen(navController = navController)
        }

        composable(Screens.Mensagens.route) {
            MensagensScreen(navController = navController)
        }

        composable(Screens.DetalhesLocal.route) { backStackEntry ->
            DetalhesLocalScreen(
                placeId = backStackEntry.arguments?.getString("placeId"),
                navController = navController
            )
        }

        composable(Screens.Perfil.route) {
            PerfilScreen(
                onLogout = {
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0)
                    }
                },
                navController = navController
            )
        }
    }
}

fun NavHostController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(0) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
