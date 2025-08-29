// CustomScaffold.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController

@Composable
fun CustomScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier, // Parâmetro modifier adicionado corretamente
    bottomBar: @Composable () -> Unit = { BottomNavigationBar(navController) },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier, // Passando o modifier para o Scaffold
        bottomBar = bottomBar,
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            content(paddingValues)
        }
    }
}