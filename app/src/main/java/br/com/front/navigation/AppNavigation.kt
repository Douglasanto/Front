
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.front.screens.cadastro.PersonScreen
import br.com.front.screens.cadastro.PersonalAddressScreen
import br.com.front.screens.login.LoginScreen
import br.com.front.screens.cadastro.SignUpScreen
import br.com.front.screens.cadastro.SocioeconomicScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screens.SignUp.route) },
            )
        }

        composable(Screens.SignUp.route) {
            SignUpScreen(
                onNavigateToPersonalData = { navController.navigate(Screens.PersonScreen.route) },
                onNavigateToLogin = { navController.navigate(Screens.Login.route) }
            )
        }


        composable(Screens.PersonScreen.route) {
            PersonScreen(
                onNavigateToPersonalAddressScreen = { navController.navigate(Screens.PersonalAddressScreen.route) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screens.PersonalAddressScreen.route) {
            PersonalAddressScreen(
              onNavigateToSocioEconomicScreen = { navController.navigate(Screens.SocioeconomicScreen.route) },
                onBack = { navController.popBackStack() },

            )
        }

        composable(Screens.SocioeconomicScreen.route) {
            SocioeconomicScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHome = {

                }
            )
        }
    }
}

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object SignUp : Screens("signup")
    object PersonScreen : Screens("PersonScreen")
    object PersonalAddressScreen : Screens("PersonalAddressScreen")
    object SocioeconomicScreen : Screens("SocioeconomicScreen")
}