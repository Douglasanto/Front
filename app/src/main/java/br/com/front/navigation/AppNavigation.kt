import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.front.navigation.Screens
import br.com.front.screens.app.avaliacoes.AvaliacaoScreen
import br.com.front.screens.app.avaliacoes.DetalhesLocalScreen
import br.com.front.screens.app.Home.HomeScreen
import br.com.front.screens.app.MensagensScreen
import br.com.front.screens.app.PerfilScreen
import br.com.front.screens.cadastro.PersonScreen
import br.com.front.screens.cadastro.PersonalAddressScreen
import br.com.front.screens.cadastro.SignUpScreen
import br.com.front.screens.cadastro.SocioeconomicScreen
import br.com.front.screens.login.LoginScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screens.SignUp.route) },
                onLoginSuccess = { navController.navigateToSingleTop(Screens.Home.route) }
            )
        }

        composable(Screens.SignUp.route) {
            SignUpScreen(
                onNavigateToPersonalData = { navController.navigate(Screens.PersonData.route) },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // Fluxo de Cadastro
        composable(Screens.PersonData.route) {
            PersonScreen(
                onNavigateToAddress = { navController.navigate(Screens.PersonalAddress.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.PersonalAddress.route) {
            PersonalAddressScreen(
                onNavigateToSocioEconomic = { navController.navigate(Screens.Socioeconomic.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.Socioeconomic.route) {
            SocioeconomicScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigateToSingleTop(Screens.Home.route) }
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

